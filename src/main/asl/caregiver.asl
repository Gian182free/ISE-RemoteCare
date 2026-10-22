// Agente Caregiver: riceve e monitora i parametri vitali del paziente

!start.

+!start : true <-
    .print("Caregiver in ascolto: pronto a ricevere gli aggiornamenti clinici.").

// Piano per gestire la ricezione dei parametri vitali
+?check_vital_signs(BPM)[source(Sender)] : BPM <= 100 <-
    .print("Parametri ricevuti da ", Sender, ": ", BPM, " BPM. Stato: NORMALE.");
    .send(Sender, tell, status_ok).

+?check_vital_signs(BPM)[source(Sender)] : BPM > 100 <-
    .print("ATTENZIONE! Battito accelerato da ", Sender, ": ", BPM, " BPM. Attivazione protocollo di allerta!");
    .send(Sender, tell, alert_triggered).