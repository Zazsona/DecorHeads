package com.zazsona.decorheads.headdata;

public class ProfileTextureData
{
    private Long timestamp;
    private String profileId;
    private String profileName;
    private ProfileTextureDataTextures textures;

    public long getTimestamp()
    {
        return timestamp;
    }

    public String getProfileId()
    {
        return profileId;
    }

    public String getProfileName()
    {
        return profileName;
    }

    public ProfileTextureDataTextures getTextures()
    {
        return textures;
    }

    public void setTimestamp(Long timestamp)
    {
        this.timestamp = timestamp;
    }

    public void setProfileId(String profileId)
    {
        this.profileId = profileId;
    }

    public void setProfileName(String profileName)
    {
        this.profileName = profileName;
    }

    public void setTextures(ProfileTextureDataTextures textures)
    {
        this.textures = textures;
    }

    public class ProfileTextureDataTextures
    {
        private ProfileTextureDataURLField SKIN; // Not sure why Mojang have this in all caps, but hey-ho

        public ProfileTextureDataURLField getSkin()
        {
            return SKIN;
        }

        public void setSkin(ProfileTextureDataURLField skin)
        {
            this.SKIN = skin;
        }
    }

    private class ProfileTextureDataURLField
    {
        private String url;

        public String getUrl()
        {
            return url;
        }

        public void setUrl(String url)
        {
            this.url = url;
        }
    }
}
