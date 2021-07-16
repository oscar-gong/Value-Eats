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
import { useHistory } from 'react-router-dom';
import { ButtonStyled } from '../styles/ButtonStyle';

const useStyles = makeStyles({
  photoCarousel: {
      justifyContent: "center",
      width: "35vw",
      height: "200px",
      objectFit: "contain",
      backgroundColor: "rgba(255, 255, 255, 0.5)",
      boxShadow: "0 4px 8px 0 rgba(0, 0, 0, 0.3)",
      padding: "10px"
  },
  name: {
    fontSize: "1.5em",
    fontWeight: "bold",
    padding: "5px 0"
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
  const history = useHistory();

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

  const handleEatery = (eateryId) => {
    history.push({
      pathname: `/EateryProfile/${eateryName}/${eateryId}`,
    })
  }

  return (
    <>
      <Box display="flex" flexDirection="column" border="2px solid white" borderRadius="20px" padding="10px" bgcolor="#E6F1D1" minWidth={onEateryProfile ? "100%" : "50vw"} maxWidth="70vw" margin="10px 0">
        <Box display="flex">
          <Box display="flex" flexDirection="column">
            <ProfilePhoto size={50} src={profilePic}></ProfilePhoto>
            <StarRating rating={editRating}></StarRating>
          </Box>
          <div style={{margin: "0px 5%", flexGrow: 1}}>
            <Box className={classes.name}>{onEateryProfile ? username : eateryName}</Box>
            <Box>{editCreateReview}</Box>
          </div>
          <Box display="flex" flexDirection="column" justifyContent="center">
            <Box display="flex" justifyContent="center">
            {
              !onEateryProfile && 
              <ButtonStyled variant="contained"
                color="primary" onClick={()=>handleEatery(eateryId)}>
                View Restaurant  
              </ButtonStyled>
            }
            {/* TODO FIX THIS BELOW */}
            {
              isOwner &&
              <IconButton onClick={() => setOpenEditCreateReview(true)}>
                <EditIcon fontSize="large" />
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
          editCreateReviewImages.length !== 0 && (
          <div style={{top: "25%", margin: "auto", outline:"none", display: "flex", justifyContent: "center"}}>
            <Carousel
              navButtonsProps={{
                  style: {
                      opacity: "50%",
                  },
              }}
              navButtonsAlwaysVisible={false}
              autoPlay={false}
            >
              {
                editCreateReviewImages.map((imgdata, idx) => {
                  return (
                    <img key={idx} 
                    alt="review photos" 
                    src={imgdata} 
                    className={classes.photoCarousel}></img>
                  );
                })
              } 
            </Carousel>
          </div>
        )}
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
