package braindustry.net;

import arc.func.Cons;
import arc.func.Cons2;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.util.Nullable;
import mindustry.net.*;

import java.io.IOException;

public class ModNet extends Net {
    private final ObjectMap<Class<?>, Cons> clientListeners = new ObjectMap<>();
    private final ObjectMap<Class<?>, Cons2<NetConnection, Object>> serverListeners = new ObjectMap<>();
    public final Net parent;
    private boolean clientLoaded;
    public ModNet(NetProvider provider,Net parent) {
        super( provider);
        this.parent=parent;
    }

    public void handleException(Throwable e) {
        parent.handleException(e);
    }

    /**
     * Display a network error. Call on the graphics thread.
     */
    public void showError(Throwable e) {
        parent.showError(e);
    }

    /**
     * Sets the client loaded status, or whether it will receive normal packets from the server.
     */
    public void setClientLoaded(boolean loaded) {
        clientLoaded=loaded;
        parent.setClientLoaded(loaded);
    }

    public void setClientConnected() {
        parent.setClientConnected();
    }

    /**
     * Connect to an address.
     */
    public void connect(String ip, int port, Runnable success) {
        parent.connect(ip, port, success);
    }

    /**
     * Host a server at an address.
     */
    public void host(int port) throws IOException {
        parent.host(port);
    }

    /**
     * Closes the server.
     */
    public void closeServer() {
        parent.closeServer();
    }

    public void reset() {
        parent.reset();
    }

    public void disconnect() {
        parent.disconnect();
    }

    public byte[] compressSnapshot(byte[] input) {
        return parent.compressSnapshot(input);
    }

    public byte[] decompressSnapshot(byte[] input, int size) {
        return parent.decompressSnapshot(input,size);
    }

    /**
     * Starts discovering servers on a different thread.
     * Callback is run on the main Arc thread.
     */
    public void discoverServers(Cons<Host> cons, Runnable done) {
        parent.discoverServers(cons,done);
    }

    /**
     * Returns a list of all connections IDs.
     */
    public Iterable<NetConnection> getConnections() {
        return parent.getConnections();
    }

    /**
     * Send an object to all connected clients, or to the server if this is a client.
     */
    public void send(Object object, SendMode mode) {
        parent.send(object, mode);
    }

    /**
     * Send an object to everyone EXCEPT a certain client. Server-side only.
     */
    public void sendExcept(NetConnection except, Object object, SendMode mode) {
        parent.sendExcept(except,object,mode);
    }

    public @Nullable
    Streamable.StreamBuilder getCurrentStream() {
        return parent.getCurrentStream();
    }

    /**
     * Registers a client listener for when an object is received.
     */
    public <T> void handleClient(Class<T> type, Cons<T> listener) {
        clientListeners.put(type, listener);
    }

    /**
     * Registers a server listener for when an object is received.
     */
    public <T> void handleServer(Class<T> type, Cons2<NetConnection, T> listener) {
        serverListeners.put(type, (Cons2<NetConnection, Object>) listener);
    }

    /**
     * Call to handle a packet being received for the client.
     */
    public void handleClientReceived(Object object) {

        try {
            boolean skip=object instanceof Packets.StreamBegin || object instanceof Packets.StreamChunk;
             if (!skip && clientListeners.get(object.getClass()) != null) {

                if (clientLoaded || ((object instanceof Packet) && ((Packet) object).isImportant())) {
                    if (clientListeners.get(object.getClass()) != null) {
                        clientListeners.get(object.getClass()).get(object);
                    }
//                    Pools.free(object);
                }
            } else {
//            Log.err("Unhandled packet type: '@'!", object);
            }
        } catch (SuccessfulException e){
            parent.handleClientReceived(object);
        } catch (Exception e){
            Log.err(e);
        }
    }

    /**
     * Call to handle a packet being received for the server.
     */
    public void handleServerReceived(NetConnection connection, Object object) {
        try {
            if (serverListeners.get(object.getClass()) != null) {
                    serverListeners.get(object.getClass()).get(connection, object);
//                Pools.free(object);
            } else {
//            Log.err("Unhandled packet type: '@'!", object.getClass());
            }
        } catch (SuccessfulException e){
            parent.handleServerReceived(connection,object);
        } catch (Exception e){
            Log.err(e);
        }

    }

    /**
     * Pings a host in an new thread. If an error occured, failed() should be called with the exception.
     */
    public void pingHost(String address, int port, Cons<Host> valid, Cons<Exception> failed) {
        parent.pingHost(address,port,valid,failed);
    }

    /**
     * Whether the net is active, e.g. whether this is a multiplayer game.
     */
    public boolean active() {
        return parent.active();
    }

    /**
     * Whether this is a server or not.
     */
    public boolean server() {
        return parent.server();
    }

    /**
     * Whether this is a client or not.
     */
    public boolean client() {
        return parent.client();
    }

    public void dispose() {
        parent.dispose();
    }

public static class SuccessfulException extends RuntimeException{

}
}
