import React from "react";

// import StarBorderRoundedIcon from '@material-ui/icons/StarBorderRounded';
// import StarHalfRoundedIcon from '@material-ui/icons/StarHalfRounded';
// import StarRoundedIcon from '@material-ui/icons/StarRounded';

// import { Box, IconButton } from '@material-ui/core';

import { Rating } from '@material-ui/lab';

export default function StarRating({rating, isEditable=false, setRating}) {
    
    return (
      <>
        <Rating value={rating}
        precision={0.5}
        onChange={(event, newValue) => {
            setRating(newValue);
        }}
        readOnly={!isEditable}
        >
        </Rating>
      </>
    );
}
