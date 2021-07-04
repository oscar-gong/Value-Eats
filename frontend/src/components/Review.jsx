import React, { useState, useContext } from 'react';
import DeleteIcon from '@material-ui/icons/Delete';
import StarRating from "../components/StarRating";
import IconButton from '@material-ui/core/IconButton';
import { ProfilePhoto } from '../styles/ProfilePhoto';
import { Box, Button } from '@material-ui/core';
import EditIcon from '@material-ui/icons/Edit';
import ConfirmModal from './ConfirmModal';
import EditReview from "../components/EditReview";
import { StoreContext } from "../utils/store";

// export default function Review ({ token, review }) {
export default function Review ({id, eateryId, token, username, profilePic, eateryName, review, rating, images}) {
  const context = useContext(StoreContext);
  const setAlertOptions = context.alert[1];
  const [openEditReview, setOpenEditReview] = useState(false);
  const [openDeleteModal, setDeleteModal] = useState(false);
  const [editReview, setEditReview] = useState(review);
  const [editRating, setEditRating] = useState(rating);
  const handleCloseModal = () => setDeleteModal(false);

  const handleDelete = async (token, id, eateryId) => {
    console.log("This will be called when a review is deleted");
    console.log(token);
    console.log(id);
    const response = await fetch("http://localhost:8080/diner/removereview", 
      {
        method: "DELETE",
        headers: {
          "Accept": "application/json",
          "Content-Type": "application/json",
          "Authorization": token,
        },
        body: JSON.stringify({
          "id": id,
          "eateryId": eateryId
        })
      });
    const responseData = await response.json();
    if (response.status === 200) {
      setAlertOptions({ showAlert: true, variant: 'success', message: responseData.message });
    } else {
      setAlertOptions({ showAlert: true, variant: 'error', message: responseData.message });
    }
    setDeleteModal(false);
  }

  return (
    <>
      <Box display="flex" border="3px solid #4F4846" bgcolor="#E8CEBF" maxWidth="70vw" marginBottom="20px">
        <Box display="flex" flexDirection="column">
          <ProfilePhoto size={50} src={profilePic}></ProfilePhoto>
          <StarRating rating={rating}></StarRating>
        </Box>
        <div style={{margin: "0px 5%"}}>
          <h2><b><u>{"Put the eatery name here"}</u></b></h2>
          <h3>{review}</h3>
        </div>
        <Box display="flex" flexDirection="column" justifyContent="center">
          <Box display="flex" justifyContent="center">
          <Button variant="contained"
            color="primary">
            View Restaurant  
          </Button>
          <IconButton>
            <EditIcon fontSize="large"
            onClick={() => setOpenEditReview(true)}/>
          </IconButton>
          <IconButton>
            <DeleteIcon fontSize="large"
            onClick={() => setDeleteModal(true)}
            />
          </IconButton>
          </Box>
        </Box>
      </Box>
      <ConfirmModal open={openDeleteModal}
        handleClose={handleCloseModal}
        eateryId={eateryId}
        title={"Delete review?"}
        message={"Are you sure you want to delete this review?"}
        handleConfirm={() => handleDelete(token, id, eateryId)}>
      </ConfirmModal>
      {/* put a carousel here */}
      <EditReview token={token} open={openEditReview} setOpen={setOpenEditReview} username={username} profilePic={profilePic} reviewTextInit={editReview} ratingInit={editRating} reviewImages={[]}/>
    </>
  )
}
