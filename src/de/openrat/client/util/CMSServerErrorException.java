package de.openrat.client.util;

public class CMSServerErrorException extends CMSException {

    private String message;
    private String status;
    private String description;
    private String reason;

    public CMSServerErrorException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public CMSServerErrorException(String message, String status, String description, String reason, Throwable cause)
    {
        super(message, cause);

        this.message = message;
        this.status = status;
        this.description = description;
        this.reason = reason;
    }

    public CMSServerErrorException(String message, String status, String description, String reason)
    {
        super(message);

        this.message = message;
        this.status = status;
        this.description = description;
        this.reason = reason;
    }

    public CMSServerErrorException(String message)
    {
        super(message);
        this.message = message;
    }

    public CMSServerErrorException(Throwable cause)
    {
        super(cause);
        this.message = cause.getMessage();
    }

    /**
     * Inhalt des Feldes <code>status</code>.
     *
     * @return status
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Inhalt des Feldes <code>description</code>.
     *
     * @return description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Inhalt des Feldes <code>reason</code>.
     *
     * @return reason
     */
    public String getReason()
    {
        return reason;
    }

    @Override
    public String getMessage()
    {
        String message = this.message + " - Status: " + this.status + ", Description: " + this.description + ", Reason: " + this.reason;

        return message;
    }

}
