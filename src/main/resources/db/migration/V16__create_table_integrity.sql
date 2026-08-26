create table tb_integrity_assessments (
  id uuid not null primary key,
  match_id uuid not null,
  score integer not null,
  risk_level varchar(20) not null,
  assessed_at timestamptz not null,
  constraint fk_integrity_assessments_match foreign key (match_id) references tb_matches(id)
);

create index idx_integrity_assessments_match_id on tb_integrity_assessments(match_id);

create table tb_integrity_factors (
  id uuid not null primary key,
  assessment_id uuid not null,
  code varchar(50) not null,
  description varchar(500) not null,
  points integer not null,
  constraint fk_integrity_factors_assessment foreign key (assessment_id) references tb_integrity_assessments(id)
);

create index idx_integrity_factors_assessment_id on tb_integrity_factors(assessment_id);