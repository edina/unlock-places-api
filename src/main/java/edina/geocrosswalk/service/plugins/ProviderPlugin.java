package edina.geocrosswalk.service.plugins;

public interface ProviderPlugin {

    /**
     * Gets the mime-type for this return format.
     * @param callback to support different content type for wrapped callbacks
     * 
     * @return the content type.
     */
    public abstract String getContentType(String callback);

    /**
     * Tests whether the plugin supports the provided format.
     * 
     * @param format the format.
     * @return <code>true</code> if the provided format is supported,
     * <code>false</code> otherwise.
     */
    public abstract boolean supports(GXWFormat format);

    /**
     * Gets the name of this export plugin.
     * 
     * @return
     */
    public abstract String getName();

    /**
     * Gets the name to be used for display to the user.
     * 
     * @return the display name.
     */
    public abstract String getDisplayName();

    /**
     * Gets a description of this plugin.
     * 
     * @return the description.
     */
    public abstract String getDescription();

    /**
     * Gets the <code>GXWFormat</code> supported by this plugin.
     * 
     * @return the supported format.
     */
    public abstract GXWFormat getSupportedFormat();

}