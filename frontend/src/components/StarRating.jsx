import React from "react";

import StarBorderRoundedIcon from "@material-ui/icons/StarBorderRounded";
import StarHalfRoundedIcon from "@material-ui/icons/StarHalfRounded";
import StarRoundedIcon from "@material-ui/icons/StarRounded";

import { Box } from "@material-ui/core";

export default function StarRating({ rating }) {
    return (
        <>
            <Box display="flex">
                {[1, 2, 3, 4, 5].map((num, key) => {
                    if (rating < num && rating > num - 1) {
                        return <StarHalfRoundedIcon key={key} />;
                    } else if (num <= rating) {
                        return <StarRoundedIcon key={key} />;
                    } else {
                        return <StarBorderRoundedIcon key={key} />;
                    }
                })}
            </Box>
        </>
    );
}
