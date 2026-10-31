// Patient agent: dormant until the scenario generator activates it, then it reports its condition.

// The activation percepts can land in any order, so each of them triggers the status report.
// The atomic guard ensures the call is sent exactly once.
+active <- !report_condition.
+my_condition(_, _) <- !report_condition.
+at(pos(_, _)) <- !report_condition.

@patient_report[atomic]
+!report_condition
    : active & my_condition(Pathology, RiskLevel) & at(pos(X, Y)) & not reported & .my_name(Me)
    <- +reported;
       .print("[PATIENT] ", Me, " reporting condition: ", Pathology, " [Risk: ", RiskLevel, "] at (", X, ",", Y, ")");
       // Routing to the control center or triage layer passing the exact risk level from the model
       .send(control_center, achieve, handle_emergency(Me, pos(X, Y), Pathology, RiskLevel)).

+!report_condition <- true.   // Not ready yet, or already reported