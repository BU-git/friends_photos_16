CREATE TABLE account_group
(
  id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  role VARCHAR(255),
  account_id BIGINT,
  group_id BIGINT
);
CREATE TABLE accounts
(
  id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  active BIT NOT NULL,
  email VARCHAR(255),
  fb_id VARCHAR(255),
  fb_profile_url VARCHAR(255),
  fb_token VARCHAR(255),
  guest BIT NOT NULL,
  password VARCHAR(255),
  profile_image_url VARCHAR(255),
  user_name VARCHAR(255),
  vk_id VARCHAR(255),
  vk_profile_url VARCHAR(255),
  vk_token VARCHAR(255)
);
CREATE TABLE comments
(
  id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  date DATETIME,
  text VARCHAR(255),
  author_id BIGINT
);
CREATE TABLE groups
(
  id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  date DATETIME,
  description VARCHAR(255),
  expire_date DATETIME,
  group_type VARCHAR(255),
  latitude DOUBLE,
  longitude DOUBLE,
  name VARCHAR(255),
  visible BIT NOT NULL,
  owner_id BIGINT
);
CREATE TABLE groups_comments
(
  Group_id BIGINT NOT NULL,
  comments_id BIGINT NOT NULL
);
CREATE TABLE photos
(
  id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  date DATETIME,
  preview_url VARCHAR(255),
  url VARCHAR(255),
  group_id BIGINT,
  owner_id BIGINT
);
CREATE TABLE photos_comments
(
  Photo_id BIGINT NOT NULL,
  comments_id BIGINT NOT NULL
);
ALTER TABLE account_group ADD FOREIGN KEY (account_id) REFERENCES accounts (id);
ALTER TABLE account_group ADD FOREIGN KEY (group_id) REFERENCES groups (id);
CREATE INDEX FKk8hu2h76qobbyl54968qygbk ON account_group (account_id);
CREATE INDEX FKklf0v50gkwrrr6ondf993ixm9 ON account_group (group_id);
ALTER TABLE comments ADD FOREIGN KEY (author_id) REFERENCES accounts (id);
CREATE INDEX FKkme10xlodeckscmx4urfb50q5 ON comments (author_id);
ALTER TABLE groups ADD FOREIGN KEY (owner_id) REFERENCES accounts (id);
CREATE INDEX FKm8hjkaiexhs4km0ewx539sk1k ON groups (owner_id);
ALTER TABLE groups_comments ADD FOREIGN KEY (Group_id) REFERENCES groups (id);
ALTER TABLE groups_comments ADD FOREIGN KEY (comments_id) REFERENCES comments (id);
CREATE UNIQUE INDEX UK_5n6j5b6piu0ilsl2ytw25k9r ON groups_comments (comments_id);
CREATE INDEX FK7olox9xr2lb2n0r9y56wpliap ON groups_comments (Group_id);
ALTER TABLE photos ADD FOREIGN KEY (owner_id) REFERENCES accounts (id);
ALTER TABLE photos ADD FOREIGN KEY (group_id) REFERENCES groups (id);
CREATE INDEX FKgy507q953m6ba1ik8vo9ew368 ON photos (owner_id);
CREATE INDEX FKii7cq96qtw9ddno2hcemefp8a ON photos (group_id);
ALTER TABLE photos_comments ADD FOREIGN KEY (Photo_id) REFERENCES photos (id);
ALTER TABLE photos_comments ADD FOREIGN KEY (comments_id) REFERENCES comments (id);
CREATE UNIQUE INDEX UK_b6d2eeh2ccei3nbrot0fce39g ON photos_comments (comments_id);
CREATE INDEX FK75om0dg3qmoti4upba83j54ob ON photos_comments (Photo_id);
