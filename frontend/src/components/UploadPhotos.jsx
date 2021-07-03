import React from 'react';
import { Box } from '@material-ui/core';
import AddAPhotoIcon from '@material-ui/icons/AddAPhoto';
import { fileToDataUrl } from '../utils/helpers';
import { ImagePreview } from '../styles/ImagePreview';
import { Label } from '../styles/Label';
import { FileUpload } from '../styles/FileUpload';

export default function UploadPhotos ({ setImages, previewImages, setPreviewImages }) {

  const handleImages = (data) => {
    Array.from(data).forEach((file) => {
        fileToDataUrl(file).then((url) => {
            setImages((prevArray) => [...prevArray, url]);
        });
    });

    if (data) {
        const fileArray = Array.from(data).map((file) =>
            URL.createObjectURL(file)
        );
        setPreviewImages(fileArray);
    }
  };

  const getPreviewImages = (data) => {
    return data.map((photo) => {
        return <ImagePreview src={photo} key={photo} />;
    });
  };

  return (
    <>
      <Box pt={2}>
        <Label>
            <FileUpload
                type="file"
                multiple
                onChange={(e) => handleImages(e.target.files)}
            />
            {<AddAPhotoIcon />} Upload Menu Photos
        </Label>
      </Box>
      <Box flex-wrap="wrap" flexDirection="row" width="60%">
          {getPreviewImages(previewImages)}
      </Box>
    </>
  )
}