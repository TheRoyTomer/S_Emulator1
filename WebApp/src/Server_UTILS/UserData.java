package Server_UTILS;

import Out.UserInfoDTO;

public class UserData {
    private final String userName;
    private int uploadedProgramCount;
    private int uploadedFunctionCount;
    private int currentCredits;
    private int usedCredits;
    private int executionCount;

    public UserData(String userName) {
        this.userName = userName;
        this.uploadedProgramCount = 0;
        this.uploadedFunctionCount = 0;
        this.currentCredits = 0;
        this.usedCredits = 0;
        this.executionCount = 0;
    }

    // Getters
    public String getUserName() { return userName; }
    public int getUploadedProgramCount() { return uploadedProgramCount; }
    public int getUploadedFunctionCount() { return uploadedFunctionCount; }
    public int getCurrentCredits() { return currentCredits; }
    public int getUsedCredits() { return usedCredits; }
    public int getExecutionCount() { return executionCount; }

    // Methods to update
    public void incrementProgramCount() { uploadedProgramCount++; }
    public void incrementFunctionCount() { uploadedFunctionCount++; }
    public void incrementExecutionCount() { executionCount++; }
    public void useCredits(int amount)
    {
        currentCredits -= amount;
        usedCredits += amount;
    }

    // Convert to DTO
    public UserInfoDTO toDTO()
    {
        return new UserInfoDTO(userName, uploadedProgramCount, uploadedFunctionCount,
                currentCredits, usedCredits, executionCount);
    }
}