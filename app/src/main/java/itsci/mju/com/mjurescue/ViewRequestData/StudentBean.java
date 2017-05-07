package itsci.mju.com.mjurescue.ViewRequestData;

/**
 * Created by Tom on 10/9/2559.
 */
public class StudentBean {
    private String studentId;
    private String studentName;
    private String idCard;
    private String phoneNumber;
    private String numberReserve;
    private String blood;
    private String disease;
    private String address;
    private String faculty;
    private String department;
    private String status;


    public StudentBean(){

    }

    public StudentBean(String studentName, String idCard, String phoneNumber, String numberReserve, String blood, String disease) {
        this.studentName = studentName;
        this.idCard = idCard;
        this.phoneNumber = phoneNumber;
        this.numberReserve = numberReserve;
        this.blood = blood;
        this.disease = disease;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNumberReserve() {
        return numberReserve;
    }

    public void setNumberReserve(String numberReserve) {
        this.numberReserve = numberReserve;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
