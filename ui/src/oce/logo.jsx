import React from 'react';

// Create a context to require images from the specified directory
const images = require.context('./resources', false, /\.(png|jpe?g|svg)$/);

const DynamicLogo = () => {
  // Get the logo image name from the environment variable
  const logoImage = process.env.REACT_APP_LOGO_IMAGE_NAME;

  // Dynamically fetch the logo using the provided image name
  let logoSrc = ''; // Initialize logoSrc to an empty string
  if (logoImage) {
    try {
      logoSrc = images(`./${logoImage}`);
      console.log('Image is: ', logoSrc);
    } catch (error) {
      console.error(`Image not found: ${logoImage}`, error);
    }
  } else {
    console.warn('No logo image name provided in environment variables.');
  }

  return (
    <div>
      {logoSrc ? (
        <img src={logoSrc} alt="@ORG_NAME@" />
      ) : (
        <p>Logo not found</p>
      )}
    </div>
  );
};

export default DynamicLogo;
