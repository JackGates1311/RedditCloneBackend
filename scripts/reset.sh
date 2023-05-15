#!/bin/bash

# Define Elasticsearch host and port
ES_HOST="localhost"
ES_PORT="9200"

# Define index names to delete documents from
INDEX_NAMES=("communities" "communities-pdf" "posts" "posts-pdf")

# Loop through index names and delete documents
for index_name in "${INDEX_NAMES[@]}"
do
  echo "Deleting documents in index ${index_name}..."
  curl -XPOST "${ES_HOST}:${ES_PORT}/${index_name}/_delete_by_query" -H 'Content-Type: application/json' -d'
  {
    "query": {
      "match_all": {}
    }
  }
  '
done

echo "Done deleting all documents in Elasticsearch indexes."

rm -rf ../documents/*
rm -rf ../images/*

echo "Done deleting all non used data"

echo "Resetting MySQL data"

# Define MySQL database credentials
DB_USER="root"
DB_PASSWORD="root"
DB_NAME="spring_reddit_clone"

# Drop database if it exists
echo "Dropping database ${DB_NAME}..."
mysql -u ${DB_USER} -p${DB_PASSWORD} -e "DROP DATABASE IF EXISTS ${DB_NAME}"

# Create database
echo "Creating database ${DB_NAME}..."
mysql -u ${DB_USER} -p${DB_PASSWORD} -e "CREATE DATABASE ${DB_NAME}"
