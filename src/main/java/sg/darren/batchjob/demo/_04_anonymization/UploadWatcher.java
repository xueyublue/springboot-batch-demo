package sg.darren.batchjob.demo._04_anonymization;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Slf4j
public class UploadWatcher {

    @Autowired
    private AnonymizationJobRun anonymizationJobRun;

    private static final String UPLOAD_DIR = "public/upload";

    @EventListener(value = ApplicationReadyEvent.class)
    public void watchAfterApplicationStarted() throws Exception {
        triggerBatchJobToProcessExistingFiles();
        createWatcherForNewFilesUpload();
    }

    private void triggerBatchJobToProcessExistingFiles() {
        FileUtils.listFiles(new File(UPLOAD_DIR), new String[]{"json"}, false)
                .forEach(file -> anonymizationJobRun.runJob(file));
    }

    private void createWatcherForNewFilesUpload() throws Exception {
        FileAlterationObserver observer = new FileAlterationObserver(UPLOAD_DIR);
        FileAlterationMonitor monitor = new FileAlterationMonitor(5_000);
        observer.addListener(new FileAlterationListenerAdaptor() {
            @Override
            public void onFileCreate(File file) {
                log.info("File created: {}", file);
            }
        });
        monitor.addObserver(observer);
        monitor.start();
    }

}
