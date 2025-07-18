import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Star } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Textarea } from '@/components/ui/textarea';
import { Label } from '@/components/ui/label';

interface ReviewFormProps {
  onSubmit: (data: { rating: number; comment: string }) => void;
  onCancel: () => void;
  isLoading?: boolean;
}

interface ReviewFormData {
  comment: string;
}

export const ReviewForm: React.FC<ReviewFormProps> = ({
  onSubmit,
  onCancel,
  isLoading = false
}) => {
  const [rating, setRating] = useState(0);
  const [hoveredRating, setHoveredRating] = useState(0);

  const { register, handleSubmit, formState: { errors } } = useForm<ReviewFormData>();

  const handleFormSubmit = (data: ReviewFormData) => {
    if (rating === 0) return;
    onSubmit({ rating, comment: data.comment || '' });
  };

  return (
    <form onSubmit={handleSubmit(handleFormSubmit)} className="space-y-4">
      <div>
        <Label className="text-base font-medium mb-2 block">
          Rating <span className="text-red-500">*</span>
        </Label>
        <div className="flex items-center space-x-1">
          {[1, 2, 3, 4, 5].map((star) => (
            <button
              key={star}
              type="button"
              onClick={() => setRating(star)}
              onMouseEnter={() => setHoveredRating(star)}
              onMouseLeave={() => setHoveredRating(0)}
              className="focus:outline-none"
            >
              <Star
                className={`h-6 w-6 cursor-pointer transition-colors ${
                  star <= (hoveredRating || rating)
                    ? 'text-yellow-400 fill-current'
                    : 'text-gray-300'
                }`}
              />
            </button>
          ))}
        </div>
        {rating === 0 && (
          <p className="text-sm text-red-600 mt-1">
            Please select a rating (required)
          </p>
        )}
      </div>

      <div>
        <Label
          htmlFor="comment"
          className="text-base font-medium mb-2 block"
        >
          Comment (optional)
        </Label>
        <Textarea
          id="comment"
          {...register('comment')}
          placeholder="Share your thoughts (optional)"
          rows={4}
        />
      </div>

      <div className="flex gap-2">
        <Button
          type="submit"
          disabled={isLoading || rating === 0}
        >
          Submit
        </Button>
        <Button
          type="button"
          variant="outline"
          onClick={onCancel}
        >
          Cancel
        </Button>
      </div>
    </form>
  );
};
