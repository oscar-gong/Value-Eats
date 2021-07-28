import React from "react";
import {
  Card,
  Grid,
  CardContent,
  CardMedia,
  Typography,
  makeStyles,
} from "@material-ui/core";
import { useHistory } from "react-router-dom";
import RatingWithNum from "../components/RatingWithNum";

const useStyles = makeStyles({
  wideCard: {
    marginTop: "50px",
    transition: "transform 0.15s ease-in-out",
    "&:hover": {
      transform: "scale3d(1.01, 1.01, 1)",
      cursor: "pointer",
      boxShadow: "0px 10px 20px rgb(0 0 0 / 0.2)",
    },
  },
});

export default function EateryDisplay ({
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
      <CardMedia
        style={{
          height: "200px",
        }}
        image={image
        }
      />
      <CardContent style={{ minHeight: "60px", maxHeight: "70px" }}>
        <Grid container justify="space-between" alignItems="flex-end">
          <Grid item xs={8}>
            <div>{`UP TO  ${discount} OFF`}</div>
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
