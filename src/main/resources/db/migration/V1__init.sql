CREATE TABLE IF NOT EXISTS video (
  id SERIAL PRIMARY KEY NOT NULL,
  size BIGINT NOT NULL,
  http_content_type VARCHAR(20) NOT NULL,
  original_file_name VARCHAR(100) NOT NULL,
  uuid VARCHAR(100) NOT NULL,
  status VARCHAR(1) NOT NULL
);

CREATE TABLE IF NOT EXISTS metadata (
  id SERIAL PRIMARY KEY NOT NULL,
  video_id INT NOT NULL,
  title VARCHAR(100) NOT NULL,
  synopsis VARCHAR(1000) NOT NULL,
  director VARCHAR(100) NOT NULL,
  actors VARCHAR(1000) NOT NULL,
  year_of_release DATE NOT NULL,
  genre VARCHAR(100) NOT NULL,
  running_time NUMERIC NOT NULL,
  status VARCHAR(1) NOT NULL,
  CONSTRAINT metadata_video_id
        FOREIGN KEY (video_id)
        REFERENCES video (id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS statistics (
  id SERIAL PRIMARY KEY NOT NULL,
  video_id INT NOT NULL,
  impression INT NOT NULL,
  view INT NOT NULL,
  CONSTRAINT statistics_video_id
      FOREIGN KEY (video_id)
      REFERENCES video (id)
      ON DELETE CASCADE
      ON UPDATE NO ACTION
);

CREATE INDEX IF NOT EXISTS video_id_idx ON statistics
(video_id ASC);

CREATE INDEX IF NOT EXISTS index_director ON metadata
USING hash(director);