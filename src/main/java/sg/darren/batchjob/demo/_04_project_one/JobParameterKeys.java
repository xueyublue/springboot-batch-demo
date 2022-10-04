package sg.darren.batchjob.demo._04_project_one;

public interface JobParameterKeys {

    public static final String INPUT_PATH = "inputPath";
    public static final String OUTPUT_PATH = "outputPath";
    public static final String CHUNK_SIZE = "chunkSize";

    public static final String INPUT_PATH_REF = "#{jobParameters['" + INPUT_PATH + "']}";
    public static final String OUTPUT_PATH_REF = "#{jobParameters['" + OUTPUT_PATH + "']}";
}
