package mapreduce

import (
	"container/list"
	"fmt"
)

type WorkerInfo struct {
	address string

	re DoJobReply // Do JobReply indicate if the worker is available or not.
}

// Clean up all workers by sending a Shutdown RPC to each one of them Collect
// the number of jobs each work has performed.
func (mr *MapReduce) KillWorkers() *list.List {
	l := list.New()
	for _, w := range mr.Workers {
		fmt.Println(w)
		DPrintf("DoWork: shutdown %s\n", w.address)
		args := &ShutdownArgs{}
		var reply ShutdownReply
		ok := call(w.address, "Worker.Shutdown", args, &reply)
		if ok == false {
			fmt.Printf("DoWork: RPC %s shutdown error\n", w.address)
		} else {
			l.PushBack(reply.Njobs)
		}
	}
	return l
}

func (mr *MapReduce) RunMaster() *list.List {

	// Get all worker info and store them on rm.
	mr.Workers = make(map[string]*WorkerInfo)

	// Assign map work to workers.
	for j := 0; j < mr.nMap; j++ {
		assigned := false
		for !assigned {
			for k, v := range mr.Workers {
				fmt.Println(k)
				if v.re.OK {
					args := &DoJobArgs{}
					args.File = mr.file
					args.JobNumber = j
					args.Operation = Map
					args.NumOtherPhase = mr.nReduce
					ok := call(k, "Worker.DoJob", args, &v.re)
					if ok {
						assigned = true
					} else {
						delete(mr.Workers, k)
					}
				}
			}
			if len(mr.Workers) == 0 {
				mr.addWorker()
			}
		}
	}

	// Assign reduce work to workers.
	for m := 0; m < mr.nReduce; m++ {
		assigned := false
		for !assigned {
			for k, v := range mr.Workers {
				if v.re.OK {
					args := &DoJobArgs{}
					args.File = mr.file
					args.JobNumber = m
					args.Operation = Reduce
					args.NumOtherPhase = mr.nMap
					ok := call(k, "Worker.DoJob", args, &v.re)
					if ok {
						assigned = true
					} else {
						delete(mr.Workers, k)
					}
				}
			}
			if len(mr.Workers) == 0 {
				mr.addWorker()
			}
		}
	}

	return mr.KillWorkers()
}

func (mr *MapReduce) addWorker() {
	wkName := <-mr.registerChannel
	temp := new(WorkerInfo)
	temp.address = wkName
	temp.re.OK = true
	mr.Workers[wkName] = temp
}
