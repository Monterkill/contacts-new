package com.happytogether.contacts.processor;

import com.happytogether.framework.log.LogBus;
import com.happytogether.framework.processor.IProcess;
import com.happytogether.framework.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

public class NonBlockingMultiThreadingProcessor implements IProcess {

    private PriorityBlockingQueue<Task> _taskQueue;
    private List<_Processor> _processors = new ArrayList<_Processor>();
    private int _processor_num = 4;

    public NonBlockingMultiThreadingProcessor(){
        _taskQueue = new PriorityBlockingQueue<Task>();
        for(int i = 0; i < _processor_num; ++i) {
            _Processor _processor = new _Processor(_taskQueue);
            _processor.start();
            _processors.add(_processor);
        }
    }

    @Override
    public void process(Task task) {
        task.setStatus(Task.WAIT);
        _taskQueue.put(task);
    }

    class _Processor extends Thread{

        private SingleThreadProcessor _processor = new SingleThreadProcessor();
        private PriorityBlockingQueue<Task> _queue;
        private boolean _quit = false;

        public _Processor(PriorityBlockingQueue<Task> task_queue){
            _queue = task_queue;
        }

        @Override
        public void run() {
            while(!_quit){
                try {
                    LogBus.Log(LogBus.DEBUGTAGS, "NonBlockingMultiThreadingProcessor - start process one");
                    _processor.process(_queue.take());
                    LogBus.Log(LogBus.DEBUGTAGS, "NonBlockingMultiThreadingProcessor - finish process one");
                }catch (Exception e){
                    LogBus.Log(LogBus.ERRORTAGS, "NonBlockingMultiThreadingProcessor - get task error");
                }
            }
        }
    }

}
