package threadfactory;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ThreadFactory;

import static org.junit.Assert.*;

public class MyThreadFactoryTest {
    String threadName;
    Thread thread;
    ThreadFactory threadFactory;

    @SuppressWarnings("all")
    @Before
    public void init() {
        threadName = "Thread";
        thread = new Thread();
        threadFactory = MyThreadFactory.getThreadFactory("Thread");
    }

    @Test
    public void getThreadFactory() {
        thread.setName("Thread");
        assertEquals("Thread 1",
                threadFactory.newThread(thread).getName());
        assertEquals("Thread 2",
                threadFactory.newThread(thread).getName());
        assertNotEquals("Thread 2",
                threadFactory.newThread(thread).getName());
    }
}