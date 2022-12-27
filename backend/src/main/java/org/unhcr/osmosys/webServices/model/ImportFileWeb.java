package org.unhcr.osmosys.webServices.model;

public class ImportFileWeb {

    private String fileName;

    private PeriodWeb period;

    private String file;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public PeriodWeb getPeriod() {
        return period;
    }

    public void setPeriod(PeriodWeb period) {
        this.period = period;
    }

    @Override
    public String toString() {
        return "ImportFileWeb{" +
                "fileName='" + fileName + '\'' +
                ", period=" + period +
                ", file='" + file + '\'' +
                '}';
    }
}
