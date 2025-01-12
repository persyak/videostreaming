# Video-Streaming-Service

### How to run:

- Clone the project
- Run in your terminal `cd project-folder/docker`
- Run from docker folder `docker-compose up -d`
- Run in your terminal `cd project-folder`
- run `./mvnw spring-boot:run` 


## API
Videos are saved into Minio storage. Metadata is saved into Postgres database. 

API URL: `http://localhost:8080/api/v1`

API Routes:
 - POST `/video/publish` - Publish video
 - DELETE `/video/{uuid}` - Delist video
 - GET `/video/load/{uuid}` - Load video content and return metadata 
 - GET `/video/{uuid}` - Play video
 - POST `/metadata/add/{uuid}` - Add metadata
 - POST `/metadata/update/{id}` - Update metadata
 - GET `/metadata` - List all videos
 - GET `/metadata/{director}` - search videos by 'director' criteria
 - GET `/statistics/{uuid}` - get particular video statistics

## BUILDING THE PROJECT
To build the project run the maven package command

This will generate a jar in the target folder. This file can be deployed on your server.