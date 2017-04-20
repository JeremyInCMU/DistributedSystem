
package cmu.edu.jiamingx1;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the cmu.edu.jiamingx1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetRoomStatusMsg_QNAME = new QName("http://jiamingx1.edu.cmu/", "getRoomStatusMsg");
    private final static QName _GetRoomStatusMsgResponse_QNAME = new QName("http://jiamingx1.edu.cmu/", "getRoomStatusMsgResponse");
    private final static QName _RoomChangeinStatus_QNAME = new QName("http://jiamingx1.edu.cmu/", "roomChangeinStatus");
    private final static QName _RoomChangeinStatusResponse_QNAME = new QName("http://jiamingx1.edu.cmu/", "roomChangeinStatusResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: cmu.edu.jiamingx1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetRoomStatusMsg }
     * 
     */
    public GetRoomStatusMsg createGetRoomStatusMsg() {
        return new GetRoomStatusMsg();
    }

    /**
     * Create an instance of {@link RoomChangeinStatusResponse }
     * 
     */
    public RoomChangeinStatusResponse createRoomChangeinStatusResponse() {
        return new RoomChangeinStatusResponse();
    }

    /**
     * Create an instance of {@link GetRoomStatusMsgResponse }
     * 
     */
    public GetRoomStatusMsgResponse createGetRoomStatusMsgResponse() {
        return new GetRoomStatusMsgResponse();
    }

    /**
     * Create an instance of {@link RoomChangeinStatus }
     * 
     */
    public RoomChangeinStatus createRoomChangeinStatus() {
        return new RoomChangeinStatus();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRoomStatusMsg }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://jiamingx1.edu.cmu/", name = "getRoomStatusMsg")
    public JAXBElement<GetRoomStatusMsg> createGetRoomStatusMsg(GetRoomStatusMsg value) {
        return new JAXBElement<GetRoomStatusMsg>(_GetRoomStatusMsg_QNAME, GetRoomStatusMsg.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRoomStatusMsgResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://jiamingx1.edu.cmu/", name = "getRoomStatusMsgResponse")
    public JAXBElement<GetRoomStatusMsgResponse> createGetRoomStatusMsgResponse(GetRoomStatusMsgResponse value) {
        return new JAXBElement<GetRoomStatusMsgResponse>(_GetRoomStatusMsgResponse_QNAME, GetRoomStatusMsgResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RoomChangeinStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://jiamingx1.edu.cmu/", name = "roomChangeinStatus")
    public JAXBElement<RoomChangeinStatus> createRoomChangeinStatus(RoomChangeinStatus value) {
        return new JAXBElement<RoomChangeinStatus>(_RoomChangeinStatus_QNAME, RoomChangeinStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RoomChangeinStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://jiamingx1.edu.cmu/", name = "roomChangeinStatusResponse")
    public JAXBElement<RoomChangeinStatusResponse> createRoomChangeinStatusResponse(RoomChangeinStatusResponse value) {
        return new JAXBElement<RoomChangeinStatusResponse>(_RoomChangeinStatusResponse_QNAME, RoomChangeinStatusResponse.class, null, value);
    }

}
