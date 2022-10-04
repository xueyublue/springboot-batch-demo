package sg.darren.batchjob.demo._04_project_one;

import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.job.DefaultJobParametersValidator;

public class ProjectOneParameterValidator extends DefaultJobParametersValidator implements JobParameterKeys {

    private static final String[] REQUIRED_KEYS = {
            INPUT_PATH,
            OUTPUT_PATH
    };

    private static final String[] OPTIONAL_KEYS = {
            CHUNK_SIZE,
    };

    public ProjectOneParameterValidator() {
        super(REQUIRED_KEYS, OPTIONAL_KEYS);
    }

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        super.validate(parameters);
        String inputPath = parameters.getString(INPUT_PATH);
        String extension = FilenameUtils.getExtension(inputPath);
        if (extension == null || !extension.equalsIgnoreCase("json")) {
            throw new JobParametersInvalidException("Input file  must be in JSON format");
        }
    }
}
