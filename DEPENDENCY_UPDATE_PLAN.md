# Dependency Update Plan - BCPARIS Service

## Executive Summary
This document outlines the dependency update strategy for the BCPARIS service to address security vulnerabilities and ensure long-term maintainability.

---

## Current Dependency Versions

| Dependency | Current Version | Latest Stable | Status | Priority |
|------------|-----------------|---------------|--------|----------|
| Spring Boot | 2.7.3 | 2.7.18 | ⚠️ OUTDATED | HIGH |
| Jackson | 2.15.0 | 2.17.2 | ⚠️ OUTDATED | HIGH |
| Log4j2 | 2.23.1 | 2.23.1 | ✅ CURRENT | - |
| Spring Cloud Sleuth | 3.1.11 | 3.1.11 | ✅ CURRENT | - |
| CXF | 4.0.7 | 4.0.7 | ✅ CURRENT | - |
| Guava | 32.0.1-jre | 33.2.1-jre | ⚠️ OUTDATED | MEDIUM |
| logback-access | 3.4.0 | 4.1.1 | ⚠️ DOWNGRADED | MEDIUM |
| dom4j | 2.1.4 | 2.1.4 | ✅ CURRENT | - |

---

## Phase 1: Critical Security Updates (Week 1-2)

### 1.1 Update Spring Boot: 2.7.3 → 2.7.18

**Why:** Security patches and bug fixes for 15 minor releases

**Risk Level:** LOW (patch version update)

**Changes Required:**
```xml
<!-- pom.xml -->
<springboot.version>2.7.18</springboot.version>
```

**Testing Required:**
- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Smoke test in DEV environment
- [ ] Verify OAuth flow works
- [ ] Check actuator endpoints

**Rollback Plan:** Revert pom.xml change

---

### 1.2 Update Jackson: 2.15.0 → 2.17.2

**Why:** Multiple CVEs fixed in versions 2.15.1 - 2.17.2

**Risk Level:** MEDIUM (minor version jump)

**Changes Required:**
```xml
<!-- pom.xml -->
<jackson.version>2.17.2</jackson.version>
```

**Known CVEs Fixed:**
- CVE-2023-35116: DOS vulnerability in Jackson 2.15.0
- CVE-2024-21634: Denial of service in specific edge cases

**Testing Required:**
- [ ] Unit tests pass
- [ ] Test JSON serialization/deserialization
- [ ] Verify Layer7Message parsing works
- [ ] Test ICBC API response parsing
- [ ] Test POR API response parsing

**Potential Breaking Changes:**
- Stricter JSON parsing (may reject previously accepted malformed JSON)
- Changes in default serialization behavior

**Rollback Plan:** Revert pom.xml change

---

### 1.3 Update Guava: 32.0.1-jre → 33.2.1-jre

**Why:** Bug fixes and performance improvements

**Risk Level:** LOW (minor API changes)

**Changes Required:**
```xml
<!-- pom.xml -->
<guava.version>33.2.1-jre</guava.version>
```

**Testing Required:**
- [ ] Unit tests pass
- [ ] Verify utility function behavior

**Rollback Plan:** Revert pom.xml change

---

### 1.4 Restore logback-access: 3.4.0 → 4.1.1

**Why:** Reverted to older version without justification

**Risk Level:** LOW (restore to latest)

**Investigation Needed:**
- Why was this downgraded?
- Check for compatibility issues with Spring Boot 2.7.18

**Changes Required:**
```xml
<!-- pom.xml -->
<dependency>
    <groupId>dev.akkinoc.spring.boot</groupId>
    <artifactId>logback-access-spring-boot-starter</artifactId>
    <version>4.1.1</version>
</dependency>
```

**Testing Required:**
- [ ] Application starts successfully
- [ ] Access logs are written correctly
- [ ] No classpath conflicts

**Rollback Plan:** Keep at 3.4.0 if compatibility issues arise

---

## Phase 2: Major Version Planning (Q3 2026)

### 2.1 Spring Boot 3.x Migration

**Current:** Spring Boot 2.7.18
**Target:** Spring Boot 3.3.x (LTS)

**Why:** Spring Boot 2.7 EOL is August 2025

**Risk Level:** HIGH (major version upgrade)

**Breaking Changes:**
- Java 17 minimum requirement (currently using Java 8)
- Jakarta EE 9+ (javax.* → jakarta.*)
- Spring Security 6.x changes
- Removed deprecated APIs

**Prerequisites:**
1. Update to Java 17
2. Update all dependencies to Jakarta-compatible versions
3. Refactor javax.* imports to jakarta.*
4. Update Spring Security configuration

**Effort Estimate:** 2-3 weeks

**Timeline:** Q3 2026 (before Spring Boot 2.7 EOL)

---

### 2.2 Replace Spring Cloud Sleuth with Micrometer Tracing

**Current:** Spring Cloud Sleuth 3.1.11
**Target:** Micrometer Tracing (included in Spring Boot 3.x)

**Why:**
- Spring Cloud Sleuth is deprecated
- Micrometer Tracing is the official replacement
- Better observability features

**Risk Level:** MEDIUM

**Changes Required:**
- Replace Sleuth dependencies with Micrometer
- Update tracing configuration
- Migrate MDC logging patterns

**Effort Estimate:** 1 week

**Timeline:** Concurrent with Spring Boot 3.x migration

---

## Implementation Steps

### Week 1: Preparation
- [ ] Create feature branch `dependency-updates-phase1`
- [ ] Document current application behavior
- [ ] Set up test environment
- [ ] Notify stakeholders

### Week 2: Updates
- [ ] Update Spring Boot to 2.7.18
- [ ] Run full test suite
- [ ] Update Jackson to 2.17.2
- [ ] Run full test suite
- [ ] Update Guava to 33.2.1-jre
- [ ] Run full test suite

### Week 3: Testing
- [ ] Deploy to DEV environment
- [ ] Perform smoke tests
- [ ] Test OAuth integration with ICBC
- [ ] Test POR integration
- [ ] Test Layer7 message processing
- [ ] Performance testing

### Week 4: Deployment
- [ ] Deploy to TEST environment
- [ ] User acceptance testing
- [ ] Deploy to PROD (with rollback plan ready)
- [ ] Monitor for 48 hours

---

## Rollback Strategy

### Automated Rollback
If deployment fails health checks:
1. OpenShift will automatically rollback to previous deployment
2. Monitor logs for error messages
3. Document failure reason

### Manual Rollback
If issues discovered post-deployment:
```bash
# Rollback OpenShift deployment
oc rollout undo deployment/bc-paris-api-oauth-new -n 91beaa-prod

# Or restore previous version
git revert <commit-hash>
mvn clean package
# Redeploy
```

---

## Testing Checklist

### Unit Tests
- [ ] All existing unit tests pass
- [ ] No new deprecation warnings
- [ ] Code coverage maintained (>70%)

### Integration Tests
- [ ] ICBC OAuth flow works
- [ ] Driver queries return correct data
- [ ] Vehicle queries return correct data
- [ ] POR queries return correct data
- [ ] Layer7 message processing works
- [ ] Error handling works correctly

### Performance Tests
- [ ] Response times within SLA (<2s)
- [ ] No memory leaks
- [ ] Token caching works
- [ ] Concurrent request handling

### Security Tests
- [ ] OAuth tokens properly masked in logs
- [ ] No sensitive data in error messages
- [ ] HTTPS endpoints only
- [ ] Dependency vulnerability scan passes

---

## Communication Plan

### Stakeholders
- Development Team
- QA Team
- DevOps Team
- Product Owner
- ICBC Integration Team

### Notifications
- **T-7 days:** Email notification of planned updates
- **T-3 days:** Reminder with deployment schedule
- **T-1 day:** Final confirmation
- **T-0:** Deployment window notification
- **T+1 day:** Post-deployment summary

---

## Success Criteria

✅ All updates deployed successfully
✅ All tests passing
✅ No increase in error rates
✅ No performance degradation
✅ No security vulnerabilities
✅ Application runs stable for 7 days

---

## Quick Reference: pom.xml Changes

```xml
<properties>
    <!-- Phase 1 Updates -->
    <springboot.version>2.7.18</springboot.version>     <!-- was 2.7.3 -->
    <jackson.version>2.17.2</jackson.version>           <!-- was 2.15.0 -->
    <guava.version>33.2.1-jre</guava.version>          <!-- was 32.0.1-jre -->

    <!-- Unchanged -->
    <log4j2.version>2.23.1</log4j2.version>
    <springcloud.version>2021.0.1</springcloud.version>
</properties>

<dependencies>
    <!-- Restore logback-access -->
    <dependency>
        <groupId>dev.akkinoc.spring.boot</groupId>
        <artifactId>logback-access-spring-boot-starter</artifactId>
        <version>4.1.1</version>                        <!-- was 3.4.0 -->
    </dependency>
</dependencies>
```

---

## Risk Assessment

| Risk | Probability | Impact | Mitigation |
|------|-------------|--------|------------|
| Breaking changes in Jackson | LOW | MEDIUM | Thorough testing, staged rollout |
| Spring Boot compatibility | LOW | LOW | Patch version only, well-tested |
| Performance degradation | VERY LOW | MEDIUM | Performance testing before PROD |
| OAuth integration breaks | LOW | HIGH | Test in DEV/TEST, keep old deployment |
| Rollback required | LOW | MEDIUM | Automated rollback ready |

---

## Long-Term Roadmap

### 2026 Q3
- [ ] Migrate to Java 17
- [ ] Migrate to Spring Boot 3.3.x
- [ ] Replace Sleuth with Micrometer Tracing

### 2026 Q4
- [ ] Evaluate Spring Boot 3.4.x (if released)
- [ ] Review and update all dependencies
- [ ] Security audit

### 2027 Q1
- [ ] Evaluate Kotlin adoption
- [ ] Review architecture for improvements

---

## Contact & Support

**Technical Lead:** TBD
**DevOps Contact:** TBD
**ICBC Integration Contact:** TBD

**Emergency Rollback:** `oc rollout undo deployment/bc-paris-api-oauth-new`

---

**Document Version:** 1.0
**Last Updated:** 2026-04-14
**Next Review:** 2026-05-14
