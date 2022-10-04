package sg.darren.batchjob.demo._04_anonymization.config;

import org.apache.commons.io.FilenameUtils;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.job.DefaultJobParametersValidator;

public class AnonymizationJobParameterValidator extends DefaultJobParametersValidator implements AnonymizationJobParameterKeys {

    private static final String[] REQUIRED_KEYS = {
            INPUT_PATH,
            OUTPUT_PATH
    };

    private static final String[] OPTIONAL_KEYS = {
    };

    public AnonymizationJobParameterValidator() {
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