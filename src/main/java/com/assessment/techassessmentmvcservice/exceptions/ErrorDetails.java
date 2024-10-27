package com.assessment.techassessmentmvcservice.exceptions;

public record ErrorDetails (
     String message,
     String details,
     int errorCode
){}


