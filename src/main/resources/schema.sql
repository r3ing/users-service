CREATE TABLE users (
  id UUID PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  created TIMESTAMP NOT NULL,
  last_login TIMESTAMP,
  token VARCHAR(255) NOT NULL,
  is_active BOOLEAN NOT NULL
);

CREATE TABLE phones (
  id INTEGER PRIMARY KEY,
  number BIGINT NOT NULL,
  city_code INTEGER NOT NULL,
  country_code VARCHAR(10) NOT NULL,
  user_id UUID NOT NULL,
  CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES users(id)
);
