import React, { useState } from 'react';
import DeleteIcon from '@material-ui/icons/Delete';
import StarRating from "../components/StarRating";
import IconButton from '@material-ui/core/IconButton';
import { ProfilePhoto } from '../styles/ProfilePhoto';
import { Box, Button } from '@material-ui/core';
import EditIcon from '@material-ui/icons/Edit';
import ConfirmModal from './ConfirmModal';

export default function Review ({token, eateryName, review, rating, images}) {

  const [openDeleteModal, setDeleteModal] = useState(false);
  const handleCloseModal = () => setDeleteModal(false);

  const handleDelete = (token, id) => {
    console.log("This will be called when a review is deleted");
    console.log(token);
    console.log(id);
  }

  return (
    <>
      <Box display="flex" border="3px solid #4F4846" bgcolor="#E8CEBF" maxWidth="70vw" marginBottom="20px">
        <Box display="flex" flexDirection="column">
          <ProfilePhoto size={50} src="https://t4.ftcdn.net/jpg/00/64/67/63/360_F_64676383_LdbmhiNM6Ypzb3FM4PPuFP9rHe7ri8Ju.jpg"></ProfilePhoto>
          <StarRating rating={rating}></StarRating>
        </Box>
        <div style={{margin: "0px 5%"}}>
          <h2><b><u>{eateryName}</u></b></h2>
          <h3>{review}</h3>
        </div>
        <Box display="flex" flexDirection="column" justifyContent="center">
          <Box display="flex" justifyContent="center">
          <Button variant="contained"
            color="primary">
            View Restaurant  
          </Button>
          <IconButton>
            <EditIcon fontSize="large"/>
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
        title={"Delete review?"}
        message={"Are you sure you want to delete this review?"}
        handleConfirm={() => handleDelete(token, eateryName)}>
      </ConfirmModal>
      {/* put a carousel here */}
    </>
  )
}
