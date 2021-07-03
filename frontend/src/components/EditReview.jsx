import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, Box, TextField , DialogActions, Button } from '@material-ui/core';
import { ProfilePhoto } from '../styles/ProfilePhoto';
import UploadPhotos from './UploadPhotos';
import StarRating from './StarRating';

export default function EditReview ({ open, setOpen, user, reviewImagesInit, reviewTextInit, ratingInit }) {

  // Will use this images as the array of strings that will be the final images that get saved
  const [ images, setImages ] = useState(reviewImagesInit);
  const [ previewImages, setPreviewImages ] = useState([]);
  const [ reviewText, setReviewText ] = useState(reviewTextInit);
  const [ rating, setRating ] = useState(ratingInit);

  const handleUpdateReview = () => {
    console.log("Make the API call here that will save this particular review for a particular restaurant");
  }

  return (
    <>
      <Dialog aria-labelledby="customized-dialog-title" open={open}>
          <DialogTitle id="customized-dialog-title">
            Edit Review
          </DialogTitle>
          <DialogContent dividers>
            <Box pt={1} display="flex" justifyContent="space-around">
              <ProfilePhoto size={70} src={user.profilePic}></ProfilePhoto>
              <h3>{user.username}</h3>
            </Box>
            <Box pt={1}>
              <StarRating rating={rating} isEditable={true} setRating={setRating}></StarRating>
            </Box>
            <UploadPhotos setImages={setImages} setPreviewImages={setPreviewImages} previewImages={previewImages}/>
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
            <Button autoFocus onClick={() => {setOpen(false); setRating(ratingInit); setReviewText(reviewTextInit); setImages(reviewImagesInit)}} color="primary">
              Cancel
            </Button>
            <Button autoFocus onClick={handleUpdateReview} color="primary">
              Save changes
            </Button>
          </DialogActions>
        </Dialog>
    </>
  )
}