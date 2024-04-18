# Check if the container name argument is provided

if ($args.Length -lt 1) {
    Write-Host "Please provide the container name as an argument."
    exit
}

# Assign the provided container name
$containerName = $args[0]
# Define variables
$databaseName = "postgres_test"
$username = "test"
$password = "test123"
$dumpFilePath = "postgres.dmp"

# Copy the dump file into the container
docker cp $dumpFilePath ${containerName}:/tmp/postgres.dmp

# Restore the dump file into the database
docker exec -it $containerName pg_restore -c -U $username -W -F c -d $databaseName postgres.dmp

# Remove the dump file from the container (optional)
docker exec -it $containerName rm /tmp/postgres.dmp