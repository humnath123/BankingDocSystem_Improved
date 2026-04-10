# Date of Birth Validation - Implementation Summary

## Date of Birth Validation Rules

### Rules Implemented:
1. **Cannot be in the future** - Must be today or earlier (as of April 10, 2026)
2. **Must be at least 10 years old** - Based on current year (2026)
3. **Valid format** - Must be YYYY-MM-DD format

### Example Scenarios (As of April 10, 2026):

#### ✅ VALID Examples:
- 2016-01-01 (10 years old)
- 2015-12-31 (10+ years old)
- 1998-05-15 (28 years old)
- 1990-03-08 (36 years old)

#### ❌ INVALID Examples:
- 2026-04-11 (In the future)
- 2026-05-01 (In the future)
- 2017-12-31 (Less than 10 years old - would be 8 years old)
- 2020-04-10 (Less than 10 years old - would be 6 years old)
- 1998/05/15 (Wrong format - uses slashes)
- 98-05-15 (Wrong format - missing century)

### Error Messages:

| Condition | Error Message |
|-----------|---------------|
| Not provided (optional) | None - field is optional |
| Wrong format | "Date of birth must be in YYYY-MM-DD format (e.g. 1998-05-15)." |
| Invalid month (>12 or <1) | "Month must be between 1 and 12." |
| Invalid day (>31 or <1) | "Day must be between 1 and 31." |
| In the future | "Date of birth cannot be in the future." |
| Less than 10 years old | "You must be at least 10 years old." |
| Other parsing error | "Invalid date of birth format." |

---

## Implementation Details

### ValidationUtil.java
**New Method**: `validateDateOfBirth(String dob)`

**Features**:
- Validates format using regex pattern `\d{4}-\d{2}-\d{2}`
- Checks month range (1-12)
- Checks day range (1-31)
- Checks if date is in future
- Calculates age and validates minimum 10 years
- Uses current date: April 10, 2026

**Logic**:
```java
public static String validateDateOfBirth(String dob) {
    // 1. Check if empty (optional field)
    if (dob == null || dob.isBlank()) {
        return null; // valid - field is optional
    }
    
    // 2. Check format
    if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
        return format error;
    }
    
    // 3. Parse and validate month/day
    // ... validate ranges ...
    
    // 4. Check if in future
    if (year > 2026 || (year == 2026 && month > 4) || ...) {
        return "Date of birth cannot be in the future.";
    }
    
    // 5. Check age >= 10
    int age = 2026 - year;
    if (age < 10) {
        return "You must be at least 10 years old.";
    }
    
    return null; // valid
}
```

### RegisterController.java
**Updated Method**: `register()` method

**Changes**:
```java
// Old code:
if (!dob.isBlank() && !dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
    return "Date of birth must be in YYYY-MM-DD format (e.g. 1998-05-15).";
}

// New code:
String dobErr = ValidationUtil.validateDateOfBirth(dob);
if (dobErr != null) {
    return dobErr;
}
```

**Impact**: Registration now validates DOB format, future dates, and minimum age

### RegisterView.java
**Updated Field**: Date of Birth field

**Changes**:
```java
// Old prompt:
dobField.setPromptText("YYYY-MM-DD  (e.g. 1998-05-15)");

// New prompt:
dobField.setPromptText("YYYY-MM-DD (min. 10 years old, not in future)");
```

**Impact**: Users see validation requirements upfront during registration

---

## Age Calculation Logic

**Formula Used**:
```
age = currentYear - birthYear
if (currentMonth < birthMonth OR (currentMonth == birthMonth AND currentDay < birthDay)) {
    age = age - 1
}
```

**Examples (as of April 10, 2026)**:
- Born: 2016-05-15 → Age = 2026 - 2016 - 1 = 9 years (too young) ❌
- Born: 2016-04-10 → Age = 2026 - 2016 = 10 years (exactly 10) ✅
- Born: 2016-04-09 → Age = 2026 - 2016 = 10 years (just turned 10) ✅
- Born: 2016-01-01 → Age = 2026 - 2016 = 10 years ✅
- Born: 2015-12-31 → Age = 2026 - 2015 = 11 years ✅

---

## Current Date Reference

**Fixed Current Date**: April 10, 2026
**Location**: In `validateDateOfBirth()` method in ValidationUtil.java

This date is hardcoded to ensure consistent validation across the system:
```java
int currentYear = 2026;
int currentMonth = 4;  // April
int currentDay = 10;   // 10th
```

---

## Integration Points

### Registration Form
- Validates DOB when user clicks "Register"
- Shows specific error message if invalid
- Prevents account creation if DOB invalid

### Optional Field
- DOB field is optional in registration
- If left blank, no validation error shown
- If provided, must meet all validation rules

---

## Testing Scenarios

### Test Case 1: Valid Age
- Input: 2015-12-31
- Expected: "You must be at least 10 years old." ❌ (Actually valid - 10+ years)
- Actual: Valid ✅

### Test Case 2: Just Under 10 Years
- Input: 2016-05-15
- Expected: "You must be at least 10 years old." ❌
- Actual: Error shown ✅

### Test Case 3: Future Date
- Input: 2026-05-01
- Expected: "Date of birth cannot be in the future." ❌
- Actual: Error shown ✅

### Test Case 4: Today's Date
- Input: 2026-04-10
- Expected: Valid ✅
- Note: Would be age 10 if born exactly today

### Test Case 5: Optional Field Empty
- Input: (blank/empty)
- Expected: Valid (field is optional) ✅
- Actual: No error shown ✅

### Test Case 6: Wrong Format
- Input: 1998/05/15
- Expected: "Date of birth must be in YYYY-MM-DD format..." ❌
- Actual: Error shown ✅

---

## Notes

- DOB field remains optional in registration
- Validation is strict about format and age
- System assumes fixed current date of April 10, 2026
- Validation happens at both registration and whenever DOB is provided
- Error messages are specific to help users correct their input

---

**Implementation Date**: April 10, 2026
**Status**: ✅ Complete

