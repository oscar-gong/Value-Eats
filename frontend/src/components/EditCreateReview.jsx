import React, { useState, useContext } from 'react';
import { Dialog, DialogTitle, DialogContent, Box, TextField , DialogActions, Button } from '@material-ui/core';
import { ProfilePhoto } from '../styles/ProfilePhoto';
import UploadPhotos from './UploadPhotos';
import StarRating from './StarRating';
import { StoreContext } from '../utils/store';

export default function EditCreateReview ({ id, eateryId, open, setOpen, username, profilePic, reviewImagesState, reviewTextState, ratingState, isEdit }) {

  const context = useContext(StoreContext);
  const setAlertOptions = context.alert[1];
  const token = context.auth[0];

  // Will use this images as the array of strings that will be the final images that get saved
  const [ images, setImages ] = useState(reviewImagesState[0]);
  const [ previewImages, setPreviewImages ] = useState(reviewImagesState[0]);
  const [ reviewText, setReviewText ] = useState(reviewTextState[0]);
  const [ rating, setRating ] = useState(ratingState[0]);

  const handleUpdateReview = async () => {
    console.log("Make the API call here that will save this particular review for a particular restaurant");
    const response = await fetch("http://localhost:8080/diner/editreview", 
      {
        method: "POST",
        headers: {
          "Accept": "application/json",
          "Content-Type": "application/json",
          "Authorization": token
        },
        body: JSON.stringify({
          "id": id,
          "eateryId": eateryId,
          "rating": rating,
          "message": reviewText,
          "reviewPhotos": images
        })
      });
    const responseData = await response.json();
    if (response.status === 200) {
      setAlertOptions({ showAlert: true, variant: 'success', message: responseData.message });
    } else {
      setAlertOptions({ showAlert: true, variant: 'error', message: responseData.message });
    }
    setOpen(false);
    reviewImagesState[1](images);
    reviewTextState[1](reviewText);
    ratingState[1](rating);
  }

  const handleCreateReview = async () => {
    const response = await fetch("http://localhost:8080/diner/createreview", 
      {
        method: "POST",
        headers: {
          "Accept": "application/json",
          "Content-Type": "application/json",
          "Authorization": token
        },
        body: JSON.stringify({
          "eateryId": eateryId,
          "rating": rating,
          "message": reviewText,
          "reviewPhotos": images
        })
      });
    const responseData = await response.json();
    if (response.status === 200) {
      setAlertOptions({ showAlert: true, variant: 'success', message: responseData.message });
    } else {
      setAlertOptions({ showAlert: true, variant: 'error', message: responseData.message });
    }
    setOpen(false);
  }

  return (
    <>
      <Dialog aria-labelledby="customized-dialog-title" open={open}>
          <DialogTitle id="customized-dialog-title">
            Edit Review
          </DialogTitle>
          <DialogContent dividers>
            <Box pt={1} display="flex" justifyContent="space-around">
              <ProfilePhoto size={70} src={profilePic}></ProfilePhoto>
              <h3>{username}</h3>
            </Box>
            <Box pt={1}>
              <StarRating rating={rating} isEditable={true} setRating={setRating}></StarRating>
            </Box>
            <UploadPhotos setImages={setImages} setPreviewImages={setPreviewImages} previewImages={previewImages} uploadDescription={"Upload Review Photos"}/>
            <Box pt={2}>
              <TextField multiline
                  id="outlined-basic"
                  label="Let us know what you think..."
                  onChange={(e) =>
                    setReviewText(e.target.value)
                  }
                  value={reviewText}
                  variant="outlined"
                  fullWidth
              />
            </Box>
          </DialogContent>
          <DialogActions>
            <Button autoFocus onClick={() => {setOpen(false); setRating(ratingState[0]); setReviewText(reviewTextState[0]); setImages(reviewImagesState[0])}} color="primary">
              Cancel
            </Button>
            <Button autoFocus onClick={isEdit ? handleUpdateReview : handleCreateReview} color="primary">
              {isEdit ? "Save changes" : "Create review"}
            </Button>
          </DialogActions>
        </Dialog>
    </>
  )
}