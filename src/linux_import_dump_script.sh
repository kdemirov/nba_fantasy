#!/bin/bash

# Check if the container name argument is provided
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <container_name>"
    exit 1
fi

# Assign the provided container name
containerName=$1

# Define other variables
databaseName="postgres"
username="test"
password="test123"
dumpFilePath="postgres.dmp"

# Copy the dump file into the container
docker cp $dumpFilePath $containerName:/postgres.dmp

# Restore the dump file into the database
docker exec -it $containerName pg_restore -c -U $username -W -F c -d $databaseName postgres.dmp

# Remove the dump file from the container (optional)
docker exec -it $containerName rm /tmp/your_dump_file.dump
