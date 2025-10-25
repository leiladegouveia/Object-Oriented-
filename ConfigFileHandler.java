package adam.base;

public record ConfigFileHandler(String[] filenames) {
    public ConfigFileHandler(String[] filenames) {
        this.filenames = filenames;
        processFilenames();
    }

    private void processFilenames() {
        for ( String filename : filenames ) {
            System.out.printf("TODO Processing file \"%s\"%n", filename);
        }
    }

    @Override
    public String[] filenames() {return filenames.clone();}
}
