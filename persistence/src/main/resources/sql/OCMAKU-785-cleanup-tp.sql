create table t_tender_status_comments as (select * from tender_status_comments where tender_id in(select id from tender where tender_process_id is null));
delete from tender_status_comments where tender_id in(select id from tender where tender_process_id is null);
create table t_tender_item as (select * from tender_item where parent_id in (select id from tender where tender_process_id is null));
delete from tender_item where parent_id in (select id from tender where tender_process_id is null);
create table t_tender_form_docs as (select * from tender_form_docs where tender_id in (select id from tender where tender_process_id is null));
delete from tender_form_docs where tender_id in (select id from tender where tender_process_id is null);
create table t_tender as (select * from tender where tender_process_id is null);
delete from tender where tender_process_id is null;

create table t_tender_quotation_evaluation_status_comments as (select * from tender_quotation_evaluation_status_comments where tender_quotation_evaluation_id
                                                                  in (select id from tender_quotation_evaluation where tender_process_id is null));
delete from tender_quotation_evaluation_status_comments where tender_quotation_evaluation_id
                                                                  in (select id from tender_quotation_evaluation where tender_process_id is null);

create table t_bid as (select * from bid where parent_id in (select id from tender_quotation_evaluation where tender_process_id is null));
delete from bid where parent_id in (select id from tender_quotation_evaluation where tender_process_id is null);
create table t_tender_quotation_evaluation_form_docs as (select * from tender_quotation_evaluation_form_docs where tender_quotation_evaluation_id in
                                                        (select id from tender_quotation_evaluation where tender_process_id is null));
delete from tender_quotation_evaluation_form_docs where tender_quotation_evaluation_id in
                                                        (select id from tender_quotation_evaluation where tender_process_id is null);
create table t_tender_quotation_evaluation as (select * from tender_quotation_evaluation where tender_process_id is null);
delete from tender_quotation_evaluation where tender_process_id is null;

create table t_professional_opinion_item_form_docs as (select * from professional_opinion_item_form_docs where professional_opinion_item_id
in (select id from professional_opinion_item where parent_id in (select id from professional_opinion where tender_process_id is null)));
delete from professional_opinion_item_form_docs where professional_opinion_item_id
in (select id from professional_opinion_item where parent_id in (select id from professional_opinion where tender_process_id is null));

create table t_professional_opinion_item as (select * from professional_opinion_item where parent_id in (select id from professional_opinion where tender_process_id is null));
delete from professional_opinion_item where parent_id in (select id from professional_opinion where tender_process_id is null);

create table t_professional_opinion_status_comments as (select * from professional_opinion_status_comments where professional_opinion_id in (select id from professional_opinion where tender_process_id is null));
delete from professional_opinion_status_comments where professional_opinion_id in (select id from professional_opinion where tender_process_id is null);

create table t_professional_opinion as (select * from professional_opinion where tender_process_id is null);
delete from professional_opinion where tender_process_id is null;

create table t_award_notification_status_comments as (select * from award_notification_status_comments where award_notification_id in (select id from
    award_notification where tender_process_id is null));
delete from award_notification_status_comments where award_notification_id in (select id from
    award_notification where tender_process_id is null);

create table t_award_notification_item_form_docs as (select *  from award_notification_item_form_docs where award_notification_item_id in (select id from award_notification_item
where parent_id in (select id from award_notification where tender_process_id is null)));
delete from award_notification_item_form_docs where award_notification_item_id in (select id from award_notification_item
where parent_id in (select id from award_notification where tender_process_id is null));

create table t_award_notification_item as (select * from award_notification_item where parent_id in (select id from award_notification where tender_process_id is null));
delete from award_notification_item where parent_id in (select id from award_notification where tender_process_id is null);

create table t_award_notification as (select *  from award_notification where tender_process_id is null);
delete from award_notification where tender_process_id is null;

create table t_award_acceptance_item_form_docs as (select * from award_acceptance_item_form_docs where award_acceptance_item_id in (select id from award_acceptance_item
    where parent_id in (select id from award_acceptance where tender_process_id is null)));
delete from award_acceptance_item_form_docs where award_acceptance_item_id in (select id from award_acceptance_item
    where parent_id in (select id from award_acceptance where tender_process_id is null));

create table t_award_acceptance_item as (select * from award_acceptance_item where parent_id in (select id from award_acceptance where tender_process_id is null));
delete from award_acceptance_item where parent_id in (select id from award_acceptance where tender_process_id is null);

create table t_award_acceptance_status_comments as (select * from award_acceptance_status_comments where award_acceptance_id in (select id from award_acceptance where tender_process_id is null));
delete from award_acceptance_status_comments where award_acceptance_id in (select id from award_acceptance where tender_process_id is null);

create table t_award_acceptance as (select * from award_Acceptance where tender_process_id is null);
delete from award_Acceptance where tender_process_id is null;

create table t_contract_status_comments as (select * from contract_status_comments where contract_id in (select id from contract where tender_process_id is null));
delete from contract_status_comments where contract_id in (select id from contract where tender_process_id is null);

create table t_contract as (select * from contract where tender_process_id is null);
delete from contract where tender_process_id is null;