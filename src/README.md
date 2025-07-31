# Create user
curl -X POST http://localhost:8888/users -H "Content-Type: application/json" -d "{\"name\":\"Manasa\",\"email\":\"manasa@example.com\"}"


# Get user
curl http://localhost:8888/users/1

# Update email
curl -X PUT http://localhost:8888/users/1/email -H "Content-Type: application/json" -d "{\"email\":\"newmansa@example.com\"}"

# Delete user
curl -X DELETE http://localhost:8888/users/1