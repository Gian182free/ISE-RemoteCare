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