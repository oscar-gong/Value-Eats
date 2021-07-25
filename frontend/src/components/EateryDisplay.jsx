import React from "react";
import {
    Card,
    Grid,
    CardContent,
    CardHeader,
    CardMedia,
    Typography,
    makeStyles,
} from "@material-ui/core";
import { useHistory } from "react-router-dom";
import RatingWithNum from "../components/RatingWithNum";

const useStyles = makeStyles({
    wideCard: {
        marginTop: "20px",
        transition: "transform 0.15s ease-in-out",
        "&:hover": {
            transform: "scale3d(1.02, 1.02, 1)",
            cursor: "pointer",
        },
    },
});

export default function EateryDisplay({
    name,
    id,
    discount,
    cuisines,
    rating,
    image
}) {
    const classes = useStyles();
    const history = useHistory();
    return (
        <Card
            className={classes.wideCard}
            onClick={(e) =>
                history.push({
                    pathname: `/EateryProfile/${name}/${id}`,
                })
            }
        >
            <CardHeader title={"UP TO " + discount + " OFF"} />
            <CardMedia
                style={{
                    height: "150px",
                }}
                //TODO change display image for restaurant
                image={image
                }
            />
            <CardContent>
                <Grid container justify="space-between" alignItems="flex-end">
                    <Grid item xs={8}>
                        <Typography variant="h5">{name}</Typography>
                        <Typography variant="subtitle2">
                            {cuisines.join(", ")}
                        </Typography>
                    </Grid>
                    <Grid item>
                        <RatingWithNum rating={rating} />
                    </Grid>
                </Grid>
            </CardContent>
        </Card>
    );
}
