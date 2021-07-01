import React from 'react';
import DeleteIcon from '@material-ui/icons/Delete';
import StarRating from "../components/StarRating";
import IconButton from '@material-ui/core/IconButton';
import { ProfilePhoto } from '../styles/ProfilePhoto';
import { Box, Button } from '@material-ui/core';
import EditIcon from '@material-ui/icons/Edit';

export default function Review () {
  return (
    <>
        <Box display="flex" border="3px solid #4F4846" bgcolor="#E8CEBF" maxWidth="70vw">
            <Box display="flex" flexDirection="column">
                <ProfilePhoto size={50} src="https://t4.ftcdn.net/jpg/00/64/67/63/360_F_64676383_LdbmhiNM6Ypzb3FM4PPuFP9rHe7ri8Ju.jpg"></ProfilePhoto>
                <StarRating rating={3.5}></StarRating>
            </Box>
            <div style={{margin: "0px 5%"}}>
                <h2><b><u>Eatery name</u></b></h2>
                <h3>Ayo, this food was ass lmao kinda like my css XD HAHABHAB</h3>
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
                    <DeleteIcon fontSize="large"/>
                </IconButton>
                </Box>
            </Box>
        </Box>
    </>
  )
}
