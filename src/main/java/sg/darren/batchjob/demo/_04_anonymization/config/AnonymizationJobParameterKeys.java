package sg.darren.batchjob.demo._04_anonymization.config;

public interface AnonymizationJobParameterKeys {

    public static final String INPUT_PATH = "inputPath";
    public static final String OUTPUT_PATH = "outputPath";
    public static final String ERROR_PATH = "errorPath";
    public static final String UPLOAD_PATH = "uploadPath";
    public static final String CHUNK_SIZE = "chunkSize";
    public static final String ANONYMIZATION_FLAG = "anonymizationFlag";

    public static final String INPUT_PATH_REF = "#{jobParameters['" + INPUT_PATH + "']}";
    public static final String OUTPUT_PATH_REF = "#{jobParameters['" + OUTPUT_PATH + "']}";
    public static final String ANONYMIZATION_FLAG_REF = "#{jobParameters['" + ANONYMIZATION_FLAG + "']}";

}
