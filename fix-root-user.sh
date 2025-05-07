#!/bin/bash

# Call the fix endpoint to create a customer profile for the root user
curl http://localhost:8888/api/fix/fix-root-user

# Call the fix endpoint to create customer profiles for all users without one
curl http://localhost:8888/api/fix/create-customer-profiles
