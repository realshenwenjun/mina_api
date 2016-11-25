/**
 *
 */
package com.mina.core;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * @author 刘飞
 */
public class FileReceiveHandler extends IoHandlerAdapter {
    private static final Logger logger = Logger.getLogger(FileReceiveHandler.class);

    private FileChannel fileChannel = null;

    /**
     *
     */
    public FileReceiveHandler() {
    }

    private void initFileChannel() throws IOException {
        String time = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(GregorianCalendar.getInstance().getTime());
        File file = new File(
                "e:\\jar\\", "test.png");

        if (!file.exists()) {
            file.createNewFile();
        } else {
            file = new File("e:\\jar\\",
                    time + "_" +
                            "test.png");
        }

        fileChannel = new FileOutputStream(file).getChannel();
    }

    private void closeFileChannel() {
        if (fileChannel != null) {
            try {
                if (fileChannel.isOpen()) {
                    FileLock fileLock = fileChannel.lock();
                    if (fileLock != null) {
                        fileLock.release();
                    }

                    fileChannel.close();
                    fileChannel = null;
                }
            } catch (IOException e) {
                logger.error("文件解锁异常。", e);
                throw new RuntimeException("文件解锁异常。", e);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.mina.core.service.IoHandlerAdapter#exceptionCaught(org.apache
     * .mina.core.session.IoSession, java.lang.Throwable)
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        closeFileChannel();
        session.close(true);
        logger.error("file upload error .", cause);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.mina.core.service.IoHandlerAdapter#messageReceived(org.apache
     * .mina.core.session.IoSession, java.lang.Object)
     */
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        if (fileChannel == null) {
            try {
                initFileChannel();
            } catch (IOException e) {
                logger.error("init FileChannel error.", e);
            }
        }

        logger.debug("Message From IP >>> " + session.getRemoteAddress().toString() + " <<<");
        IoBuffer ib = (IoBuffer) message;
        logger.debug("\n\t\tDATA >>> " + message.toString() + "\n\t\tDATA SIZE >>> " + ib.capacity() + "\n");
        fileChannel.write(ib.buf());
        closeFileChannel();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.mina.core.service.IoHandlerAdapter#messageSent(org.apache.
     * mina.core.session.IoSession, java.lang.Object)
     */
    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        super.messageSent(session, message);
        logger.debug("super.messageSent(IoSession session, Object message)\n\t\t >>> " + session.getRemoteAddress().toString() + " <<<");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.mina.core.service.IoHandlerAdapter#sessionClosed(org.apache
     * .mina.core.session.IoSession)
     */
    @Override
    public void sessionClosed(IoSession session) throws Exception {
        super.sessionClosed(session);
        logger.debug("super.sessionClosed(IoSession session)\n\t\t >>> " + session.getRemoteAddress().toString() + " <<<");
        closeFileChannel();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.mina.core.service.IoHandlerAdapter#sessionCreated(org.apache
     * .mina.core.session.IoSession)
     */
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        super.sessionCreated(session);
        logger.debug("super.sessionCreated(IoSession session)\n\t\t >>> " + session.getRemoteAddress().toString() + " <<<");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.mina.core.service.IoHandlerAdapter#sessionIdle(org.apache.
     * mina.core.session.IoSession, org.apache.mina.core.session.IdleStatus)
     */
    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {
        super.sessionIdle(session, status);
        logger.debug("super.sessionIdle(IoSession session, IdleStatus status)\n\t\t >>> " + session.getRemoteAddress().toString() + " <<<");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.apache.mina.core.service.IoHandlerAdapter#sessionOpened(org.apache
     * .mina.core.session.IoSession)
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
        super.sessionOpened(session);
        logger.debug("super.sessionOpened(IoSession session)\n\t\t >>> " + session.getRemoteAddress().toString() + " <<<");
    }
}
