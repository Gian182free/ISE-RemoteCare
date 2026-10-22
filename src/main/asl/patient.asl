// Agente Paziente: simula l'invio dei parametri vitali al caregiver

!start.

+!start : true <-
    .print("Paziente avviato: monitoraggio dei parametri vitali in corso...");
    .wait(4000); // attende 4 secondi prima di inviare i dati
    .print("Invio dei dati biometrici (es. battito cardiaco) al caregiver...");
    .send(caregiver_agent, achieve, check_vital_signs(75)).