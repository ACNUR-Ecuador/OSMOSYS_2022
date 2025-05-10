package org.unhcr.osmosys.services;

import org.unhcr.osmosys.model.JobStatus;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JobStatusService {
    // Mapa para almacenar el estado de cada tarea, identificado por jobId.
    private static final Map<String, JobStatus> jobStatusMap = new ConcurrentHashMap<>();

    // Crea o inicializa un nuevo JobStatus para un jobId dado.
    public static void createJob(String jobId) {
        jobStatusMap.put(jobId, new JobStatus(0, "Iniciado"));
    }

    // Crea o inicializa un nuevo JobStatus para un jobId dado.
    public static void finalizeJob(String jobId, Object response) {
        jobStatusMap.computeIfPresent(jobId, (id, jobStatus) -> {
            jobStatus.setProgress(100);
            jobStatus.setState("Completado");
            jobStatus.setResponse(response);
            return jobStatus;
        });    }

    // Actualiza el progreso manteniendo el estado actual.
    public static void setProgress(String jobId, int progress) {
        jobStatusMap.computeIfPresent(jobId, (id, jobStatus) -> {
            jobStatus.setProgress(progress);
            return jobStatus;
        });
    }

    // Actualiza el estado manteniendo el progreso actual.
    public static void setState(String jobId, String state) {
        jobStatusMap.computeIfPresent(jobId, (id, jobStatus) -> {
            jobStatus.setState(state);
            return jobStatus;
        });
    }

    // Actualiza el estado manteniendo el progreso actual.
    public static void setJob(String jobId,int progress, String state) {
        jobStatusMap.computeIfPresent(jobId, (id, jobStatus) -> {
            jobStatus.setState(state);
            jobStatus.setProgress(progress);
            return jobStatus;
        });
    }

    // Actualiza ambos valores.
    public static void updateJob(String jobId, int progress, String state) {
        if(jobId != null) {
            jobStatusMap.put(jobId, new JobStatus(progress, state));
        }
    }

    // Recupera el JobStatus asociado a un jobId.
    public static JobStatus getJobStatus(String jobId) {
        int progress = jobStatusMap.get(jobId).getProgress();
        JobStatus jobStatus = new JobStatus(jobStatusMap.get(jobId));
        if(progress == 100) {
            jobStatusMap.remove(jobId);
        }
        return jobStatus;
    }

    /***
     *
     * @return
     */
    public static String createJob() {
        String uuid =  UUID.randomUUID().toString();
        JobStatusService.createJob(uuid);
        return uuid;
    }
}