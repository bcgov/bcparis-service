# OAuth Production Deployment Guide

## Overview

This guide describes the blue-green deployment strategy for deploying OAuth to production.

### Deployment Architecture

- **Blue (Current)**: `bc-paris-api` - existing deployment without OAuth
- **Green (New)**: `bc-paris-api-oauth` - new deployment with OAuth support

## Prerequisites

### OAuth Configuration

The following OAuth credentials are required for production:

- **Client ID**: `f1d5ea7738ac16350f6feb0bf8489e1c`
- **Client Secret**: `c04f518a72fb0881ded29c809a2a377b`
- **Token URL**: `https://wsgw.jag.gov.bc.ca/icbc-oauth2-token`
- **REST API URL**: `https://wsgw.jag.gov.bc.ca/icbc/external/integration/bcag-informationlookup-api/v1/information`

## Deployment Steps

### Step 1: Create Production OAuth Secret

```bash
oc project 91beaa-prod

oc create secret generic icbc-oauth-config-new \
  --from-literal=ENDPOINT_ICBC_OAUTH_CLIENT_ID=f1d5ea7738ac16350f6feb0bf8489e1c \
  --from-literal=ENDPOINT_ICBC_OAUTH_SECRET=c04f518a72fb0881ded29c809a2a377b \
  --from-literal=ENDPOINT_ICBC_OAUTH_TOKEN_URL=https://wsgw.jag.gov.bc.ca/icbc-oauth2-token \
  --from-literal=ENDPOINT_ICBC_REST_URL=https://wsgw.jag.gov.bc.ca/icbc/external/integration/bcag-informationlookup-api/v1/information
```

Alternatively, use the template:
```bash
oc apply -f openshift/prod-oauth-secret-template.yaml
```

### Step 2: Build and Push OAuth Image

Run the GitHub Actions workflow "Main - Build Image and Push":
```
App Name: bc-paris-api
Image Target Env: dev (or specify oauth tag)
```

The image will be tagged as `bc-paris-api:oauth`

### Step 3: Deploy Green (OAuth) Environment

```bash
# Apply the OAuth deployment
oc apply -f openshift/prod-oauth-deployment.yaml

# Apply the OAuth service
oc apply -f openshift/prod-oauth-service.yaml

# Verify deployment
oc get deployment bc-paris-api-oauth -n 91beaa-prod
oc get pods -l app=bc-paris-api-oauth -n 91beaa-prod
```

### Step 4: Test Green Environment

```bash
# Get the service endpoint
oc get svc bc-paris-api-oauth -n 91beaa-prod

# Port forward for testing
oc port-forward svc/bc-paris-api-oauth 8080:8080 -n 91beaa-prod

# Test the endpoint
curl http://localhost:8080/actuator/health
```

### Step 5: Switch Traffic to Green (OAuth)

Update the production route to point to the new service:

```bash
# Check current route
oc get route bc-paris-api -n 91beaa-prod -o yaml

# Update route to point to OAuth service
oc patch route bc-paris-api -n 91beaa-prod \
  --type=json \
  -p='[{"op": "replace", "path": "/spec/to/name", "value": "bc-paris-api-oauth"}]'

# Verify route is updated
oc get route bc-paris-api -n 91beaa-prod -o jsonpath='{.spec.to.name}'
```

### Step 6: Monitor and Verify

```bash
# Check logs
oc logs -f deployment/bc-paris-api-oauth -n 91beaa-prod

# Check OAuth token requests
oc logs deployment/bc-paris-api-oauth -n 91beaa-prod | grep -i "oauth"

# Monitor pod health
oc get pods -l app=bc-paris-api-oauth -n 91beaa-prod -w
```

## Rollback Procedure

If issues occur, switch traffic back to blue (current) deployment:

```bash
# Switch route back to original service
oc patch route bc-paris-api -n 91beaa-prod \
  --type=json \
  -p='[{"op": "replace", "path": "/spec/to/name", "value": "bc-paris-api"}]'

# Verify rollback
oc get route bc-paris-api -n 91beaa-prod -o jsonpath='{.spec.to.name}'
```

## Cleanup (After Successful Deployment)

Once OAuth deployment is stable and verified:

```bash
# Scale down old deployment
oc scale deployment bc-paris-api --replicas=0 -n 91beaa-prod

# Optional: Delete old deployment (after extended monitoring period)
# oc delete deployment bc-paris-api -n 91beaa-prod
```

## Traffic Splitting (Optional)

For gradual rollout, you can split traffic between blue and green:

```bash
# Get current route weight
oc get route bc-paris-api -n 91beaa-prod -o yaml

# Add alternate backend with weight
oc set route-backends bc-paris-api \
  bc-paris-api=50 \
  bc-paris-api-oauth=50 \
  -n 91beaa-prod

# Gradually increase OAuth traffic
oc set route-backends bc-paris-api \
  bc-paris-api=25 \
  bc-paris-api-oauth=75 \
  -n 91beaa-prod

# Full cutover to OAuth
oc set route-backends bc-paris-api \
  bc-paris-api-oauth=100 \
  -n 91beaa-prod
```

## Verification Checklist

- [ ] OAuth secret created in production
- [ ] OAuth image built and tagged
- [ ] Green deployment running with 2/2 pods ready
- [ ] Green service created and accessible
- [ ] Health check endpoint responding
- [ ] OAuth token acquisition working (check logs)
- [ ] ICBC API calls successful
- [ ] Route updated to point to green service
- [ ] Production traffic flowing through OAuth service
- [ ] No errors in application logs
- [ ] Monitoring dashboards showing normal metrics

## Troubleshooting

### OAuth Token Errors

```bash
# Check OAuth configuration
oc get secret icbc-oauth-config-new -n 91beaa-prod -o jsonpath='{.data}' | jq

# Verify token URL is accessible
oc exec -it deployment/bc-paris-api-oauth -n 91beaa-prod -- \
  curl -v https://wsgw.jag.gov.bc.ca/icbc-oauth2-token
```

### Pod Not Starting

```bash
# Check pod events
oc describe pod -l app=bc-paris-api-oauth -n 91beaa-prod

# Check pod logs
oc logs -l app=bc-paris-api-oauth -n 91beaa-prod --tail=100
```

### Secret Not Found

```bash
# Verify secret exists
oc get secret icbc-oauth-config-new -n 91beaa-prod

# Check secret is mounted in pod
oc exec -it deployment/bc-paris-api-oauth -n 91beaa-prod -- env | grep ICBC
```

## Contact

For issues or questions, contact the development team.
