% STRIPS domain actions for RemoteCare

action(check_vitals(Patient),
        [pending_check(Patient), sensor_active(Patient)],
        [vitals_checked(Patient)],
        [pending_check(Patient)]).

action(acquire(Equipment),
        [available(Equipment), needed(Equipment)],
        [held(Equipment)],
        [available(Equipment)]).

action(release(Equipment),
        [held(Equipment)],
        [available(Equipment)],
        [held(Equipment)]).

action(treat_patient(Patient, Code),
        [vitals_checked(Patient), treatment_resources_for(Code)],
        [treated(Patient)],
        []).

action(discharge(Patient),
        [treated(Patient), nothing_held],
        [discharged(Patient)],
        []).

% Support and state verification rules
holds_in(Condition, State) :- member(Condition, State).

holds_in(all_checks_done, State) :- \+ member(pending_check(_), State).
holds_in(nothing_held, State) :- \+ member(held(_), State).

% Generation of the clinical intervention plan
care_plan(Patient, Pathology, Code, Plan) :-
    initial_state(Patient, Pathology, Code, State),
    plan(State, [discharged(Patient)], Plan).

initial_state(Patient, _Pathology, Code, State) :-
    findall(pending_check(Patient), true, Pending),
    treatment_extras(Code, Extras),
    append(Pending, [sensor_active(Patient)], S1),
    append(S1, Extras, State).

treatment_extras(red, [available(icu_bed), needed(icu_bed)]).
treatment_extras(yellow, [available(teleconsult), needed(teleconsult)]).
treatment_extras(green, []).


% Clinical Thresholds (Single Source of Truth)

% Soglie critiche per il livello RED
threshold(spo2, red, max_exclusive, 90).
threshold(bp, red, high_inclusive, 180).
threshold(bp, red, low_inclusive, 80).
threshold(hr, red, high_inclusive, 140).
threshold(hr, red, low_inclusive, 45).
threshold(glucose, red, high_inclusive, 300).
threshold(glucose, red, low_inclusive, 50).

% Soglie di guardia per il livello YELLOW
threshold(spo2, yellow, max_inclusive, 94).
threshold(bp, yellow, high_inclusive, 140).
threshold(bp, yellow, low_inclusive, 90).
threshold(hr, yellow, high_inclusive, 100).
threshold(hr, yellow, low_inclusive, 55).
threshold(glucose, yellow, high_inclusive, 200).
threshold(glucose, yellow, low_inclusive, 70).

% Dynamic Risk Evaluation Rules (Generic)

evaluate_risk(HR, BP, SpO2, Glucose, red) :-
    ( threshold(spo2, red, max_exclusive, SpO2Max), SpO2 < SpO2Max ;
      threshold(bp, red, high_inclusive, BPHigh), BP > BPHigh ;
      threshold(bp, red, low_inclusive, BPLow), BP < BPLow ;
      threshold(hr, red, high_inclusive, HRHigh), HR > HRHigh ;
      threshold(hr, red, low_inclusive, HRLow), HR < HRLow ;
      threshold(glucose, red, high_inclusive, GluHigh), Glucose > GluHigh ;
      threshold(glucose, red, low_inclusive, GluLow), Glucose < GluLow ),
    !.

evaluate_risk(HR, BP, SpO2, Glucose, yellow) :-
    ( threshold(spo2, yellow, max_inclusive, SpO2Warn), SpO2 =< SpO2Warn ;
      threshold(bp, yellow, high_inclusive, BPHighW), BP > BPHighW ;
      threshold(bp, yellow, low_inclusive, BPLowW), BP < BPLowW ;
      threshold(hr, yellow, high_inclusive, HRHighW), HR > HRHighW ;
      threshold(hr, yellow, low_inclusive, HRLowW), HR < HRLowW ;
      threshold(glucose, yellow, high_inclusive, GluHighW), Glucose > GluHighW ;
      threshold(glucose, yellow, low_inclusive, GluLowW), Glucose < GluLowW ),
    !.

evaluate_risk(_, _, _, _, green).