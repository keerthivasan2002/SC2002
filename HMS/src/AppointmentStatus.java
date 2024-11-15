public enum AppointmentStatus {
    CONFIRMED, //after the appointment has beeen accepted by the doctor but not completed
    CANCELLED, //the patient cancelled their appointment
    COMPLETED, //the patient has already attended the appointment
    REJECTED, //the doctor rejected the appointment
    PENDING, //the patient is waiting for the doctor to reject or approve
    APPROVED, //this is same as confirmed?
    SCHEDULED //this is same as pending?
}
