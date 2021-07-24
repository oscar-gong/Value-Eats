import React from "react";

// import StarBorderRoundedIcon from '@material-ui/icons/StarBorderRounded';
// import StarHalfRoundedIcon from '@material-ui/icons/StarHalfRounded';
// import StarRoundedIcon from '@material-ui/icons/StarRounded';

// import { Box, IconButton } from '@material-ui/core';

import { Rating } from '@material-ui/lab';
import StarBorderIcon from '@material-ui/icons/StarBorder';

export default function StarRating({rating, isEditable=false, setRating}) {
    
    return (
      <>
        <Rating value={rating}
        precision={0.5}
        onChange={(event, newValue) => {
            setRating(newValue);
        }}
        emptyIcon={<StarBorderIcon style={{color: "#FFB400"}} fontSize="inherit" />}
        readOnly={!isEditable}
        >
        </Rating>
      </>
    );
}
