create table if not exists calculations (
  id bigserial primary key,

  left_value varchar(256) not null,
  left_radix varchar(16) not null,

  right_value varchar(256) not null,
  right_radix varchar(16) not null,

  operation varchar(16) not null,

  result_value varchar(256) not null,
  result_radix varchar(16) not null,

  created_at timestamptz not null default now()
);

create index if not exists idx_calculations_created_at on calculations (created_at);
create index if not exists idx_calculations_operation on calculations (operation);
create index if not exists idx_calculations_radixes on calculations (left_radix, right_radix);
