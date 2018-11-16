package a6test.kayd;

	import static org.junit.Assert.*;

	import org.junit.Test;

	import a6.*;
import a6Tests.ROIObserverImpl;

public class A6Tests {
	Pixel red = new ColorPixel(1.0,0.0,0.0);
	Pixel green = new ColorPixel(0.0,1.0,0.0);
	Pixel blue = new ColorPixel(0.0,0.0,1.0);
	Pixel yellow = new ColorPixel(1.0,1.0,0.0);
	Pixel cyan = new ColorPixel(0.0,1.0,1.0);
	Pixel magenta = new ColorPixel(1.0,0.0,1.0);
	Pixel white = new ColorPixel(1.0,1.0,1.0);
	Pixel black = new ColorPixel(0.0,0.0,0.0);
	Pixel randomOne = new ColorPixel(0.5,0.6,0.7);
	Pixel randomTwo = new ColorPixel(0.6,0.8,0.1);
	Pixel randomThree = new ColorPixel(0.7,0.1,0.3);
	Pixel randomFour = new ColorPixel(0.4,0.6,0.1);
	Pixel randomFive = new ColorPixel(0.5,0.3,0.9);
	Pixel randomSix = new ColorPixel(0.1,0.2,0.4);
	Pixel randomSeven = new ColorPixel(0.1,0.7,0.3);
	Pixel randomEight = new ColorPixel(0.4,0.5,0.3);
	Pixel randomNine = new ColorPixel(0.1,0.15,0.6);	
	Pixel[][] randomArrayOne = {{randomOne, randomTwo, randomThree},
			 					{randomFour, randomFive, randomSix},
			 					{randomSeven, randomEight, randomNine}};
	Pixel[][] largeRandomArray = {{randomOne, randomTwo, randomThree, randomFour},
								  {randomFive, randomSix, randomSeven, randomEight},
								  {randomNine, randomOne, randomTwo, randomThree},
								  {randomFour, randomFive, randomSix, randomSeven}};
	Pixel[][] randomArrayTwo = {{red, green},    
								{blue, yellow}};
																										     	// C M M M R
	
	Picture randomPictureLarge = new MutablePixelArrayPicture(randomArrayOne, "random picture");
	Picture immutableSquare = new ImmutablePixelArrayPicture(largeRandomArray, "the large immutable");
	Picture smallRandom = new MutablePixelArrayPicture(randomArrayTwo, "It's a little random");

	ObservablePicture random = new ObservablePictureImpl(randomPictureLarge);
	ObservablePicture immutable = new ObservablePictureImpl(immutableSquare);
	ObservablePicture smallRandomObserve = new ObservablePictureImpl(smallRandom);
	
		Region one = new RegionImpl(1,3,3,4);
		Region two = new RegionImpl(0,0,4,4);
		Region intersectRegion = new RegionImpl(1,3,3,4);
		Region unionRegion = new RegionImpl(0,0,4,4);
		ROIObserver thisOne = new ROIObserverImpl();
		ROIObserver thatOne = new ROIObserverImpl();
		
		@Test
		public void testObservablePaintMutability() {
			Picture temp =immutable.paint(1,1,2,2, white);
			if (temp != immutable) {
				fail("Did not produce the same picture - did not replace source with returned picture.");
			}
			Picture tempTwo = immutable.paint(2, 4, 5, 6, black);
			if (tempTwo != immutable) {
				fail("Did not reproduce new picture");
			}
		}
			

		@Test
		public void checkForNullPictureInConstructor() {
			try {
				Picture nullValue = new ObservablePictureImpl(null);
			} catch (IllegalArgumentException e) {
			}
		}
		
		@Test
		public void checkForFind() {
			random.registerROIObserver(thisOne, one);
			ROIObserver[] onlyOne = random.findROIObservers(one);
			if (onlyOne[0] != thisOne) {
				fail("Returned incorrect array output");
			}
			random.registerROIObserver(thatOne, one);
			ROIObserver[] nowTheresTwo = random.findROIObservers(two);
			if (nowTheresTwo[0] != thisOne ||
					nowTheresTwo[1] != thatOne) {
				fail("Should have returned both ROIOBservers");
			}
		}
		public boolean isSameRegion(Region a, Region b) {
			if (a.getTop() == b.getTop() &&
					a.getLeft() == b.getLeft() &&
					a.getRight() == b.getRight() &&
					a.getBottom() == b.getBottom()) {
				return true;
			}
			else {
				return false;
			}
		}
		@Test
		public void checkForIntersection() {
			try {
				Region tempIntersect = one.intersect(two);
				if (!isSameRegion(tempIntersect, intersectRegion)) {
					fail("The intersect region should have been returned");
				}
				} catch (NoIntersectionException e) {
					fail("There should have been an intersection");
				}
		}
		
		@Test
		public void checkForUnion() {
			Region unionRegionTemp = one.union(null);
			if (!isSameRegion(unionRegionTemp, one)) {
				fail("Did not return original region");
			}
			unionRegionTemp = one.union(two);
			if (!isSameRegion(unionRegion, unionRegionTemp)) {
				fail("Did not return the union of the regions");
			}
		}

}