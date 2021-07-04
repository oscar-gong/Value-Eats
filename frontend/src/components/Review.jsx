import React, { useState, useContext } from 'react';
import DeleteIcon from '@material-ui/icons/Delete';
import StarRating from "../components/StarRating";
import IconButton from '@material-ui/core/IconButton';
import { ProfilePhoto } from '../styles/ProfilePhoto';
import { Box, Button, Divider, makeStyles } from '@material-ui/core';
import EditIcon from '@material-ui/icons/Edit';
import ConfirmModal from './ConfirmModal';
import EditCreateReview from "../components/EditCreateReview";
import { StoreContext } from "../utils/store";
import Carousel from 'react-material-ui-carousel';

const useStyles = makeStyles({
  photo: {
      color: "black",
      transition: "transform 0.15s ease-in-out",
      "&:hover": { transform: "scale3d(1.02, 1.02, 1)", maxHeight: "none" },
      width: "150px",
      height: "150px",
      boxShadow: "0 4px 8px 0 rgba(0, 0, 0, 0.3)",
      objectFit: "contain",
      backgroundColor: "white",
  },
  photoCarousel: {
      width: "400px",
      height: "400px",
      boxShadow: "0 4px 8px 0 rgba(0, 0, 0, 0.3)",
      objectFit: "contain",
      backgroundColor: "white",
      padding: "100px",
  },
  gridContainer: {
      background: "rgba(255, 255, 255, 0.2)",
      marginTop: "10px",
      borderRadius: "10px",
      
  },
  subtitle: {
      background: "rgba(255, 255, 255, 0.5)",
      borderRadius: "10px",
      margin: "10px 0px",
      padding: "5px 0px",
  }
});
// export default function Review ({ token, review }) {
export default function Review ({id, eateryId, username, profilePic, eateryName, review, rating, images, onEateryProfile, isOwner}) {
  const context = useContext(StoreContext);
  const setAlertOptions = context.alert[1];
  const token = context.auth[0];
  
  const classes = useStyles();
  const [openEditCreateReview, setOpenEditCreateReview] = useState(false);
  const [openDeleteModal, setDeleteModal] = useState(false);
  const [editCreateReview, setEditCreateReview] = useState(review);
  const [editRating, setEditRating] = useState(rating);
  const [editCreateReviewImages, setEditCreateReviewImages] = useState(images);
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
      <Box display="flex" flexDirection="column" border="3px solid #4F4846" bgcolor="#E8CEBF" minWidth="50vw" maxWidth="70vw" marginBottom="20px">
        <Box display="flex">
          <Box display="flex" flexDirection="column">
            <ProfilePhoto size={50} src={profilePic}></ProfilePhoto>
            <StarRating rating={editRating}></StarRating>
          </Box>
          <div style={{margin: "0px 5%", flexGrow: 1}}>
            <h2><b><u>{eateryName}</u></b></h2>
            <h3>{editCreateReview}</h3>
          </div>
          <Box display="flex" flexDirection="column" justifyContent="center">
            <Box display="flex" justifyContent="center">
            {
              onEateryProfile && 
              <Button variant="contained"
                color="primary">
                View Restaurant  
              </Button>
            }
            {/* TODO FIX THIS BELOW */}
            {
              isOwner &&
              <IconButton>
                <EditIcon fontSize="large"
                onClick={() => setOpenEditCreateReview(true)}/>
              </IconButton>
            }
            {
              isOwner &&
              <IconButton onClick={() => setDeleteModal(true)}>
                <DeleteIcon fontSize="large" />
              </IconButton>
            }
            </Box>
          </Box>
        </Box>
        {
          editCreateReviewImages.length !== 0 &&
          <Box paddingY="30px">
            <Divider variant="middle" />
          </Box>
        }
        {
          editCreateReviewImages.length !== 0 &&
          <Carousel
            navButtonsProps={{
                style: {
                    opacity: "50%",
                },
            }}
            navButtonsAlwaysVisible={true}
            autoPlay={false}
          >
            {
              editCreateReviewImages.map((imgdata, idx) => {
                return (
                  // Temp REPLACE WITH A CAROUSEL LATER
                  <img key={idx} alt="review photos" src={imgdata} className={classes.photoCarousel}></img>
                );
              })
            } 
          </Carousel>
        }
      </Box>
      <ConfirmModal open={openDeleteModal}
        handleClose={handleCloseModal}
        eateryId={eateryId}
        title={"Delete review?"}
        message={"Are you sure you want to delete this review?"}
        handleConfirm={() => handleDelete(token, id, eateryId)}>
      </ConfirmModal>
      {/* put a carousel here */}
      <EditCreateReview id={id} eateryId={eateryId} open={openEditCreateReview} setOpen={setOpenEditCreateReview} username={username} profilePic={profilePic} reviewTextState={[editCreateReview, setEditCreateReview]} ratingState={[editRating, setEditRating]} reviewImagesState={[editCreateReviewImages, setEditCreateReviewImages]} isEdit={true}/>
    </>
  )
}
